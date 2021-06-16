package com.rs.lib.net.decoders;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rs.lib.Globals;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.Decoder;
import com.rs.lib.net.Session;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;
import com.rs.lib.util.Logger;
import com.rs.lib.util.Utils;

public final class GameDecoder extends Decoder {
	
	private static Map<ClientPacket, Packet> PACKET_DECODERS = new HashMap<>();
	
	public GameDecoder(Session session) {
		super(session);
	}
	
	public static void loadPacketDecoders() throws InvocationTargetException, NoSuchMethodException {
		try {
			Logger.log("WorldPacketsDecoder", "Initializing packet decoders...");
			ArrayList<Class<?>> classes = Utils.getClassesWithAnnotation("com.rs.lib.net.packets.decoders", PacketDecoder.class);
			for (Class<?> clazz : classes) {
				ClientPacket[] packets = clazz.getAnnotation(PacketDecoder.class).value();
				for (ClientPacket packet : packets) {
					if (PACKET_DECODERS.put(packet, (Packet) clazz.getConstructor().newInstance()) != null)
						System.err.println("Duplicate decoders for packet: " + packet);
				}
			}
			
			Set<ClientPacket> missing = new HashSet<>();
			for (ClientPacket packet : ClientPacket.values()) {
				if (PACKET_DECODERS.get(packet) == null) {
					missing.add(packet);
				}
			}
			
			int handled = ClientPacket.values().length - missing.size();
			Logger.log("WorldPacketsDecoder", "Packet decoders loaded for " + handled + " packets...");
			Logger.log("WorldPacketsDecoder", "Packets missing: " + missing);
		} catch (ClassNotFoundException | IOException | IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int decode(InputStream stream) {
		while (stream.getRemaining() > 0) {
			int start = stream.getOffset();
			int opcode = stream.readPacket(session.getIsaac());
			ClientPacket packet = ClientPacket.forOpcode(opcode);
			if (packet == null) {
				if (Globals.DEBUG)
					System.out.println("Invalid opcode: " + opcode + ".");
				return -1;
			}

			int length = packet.getSize();
			if ((length == -1 && stream.getRemaining() < 1) || (length == -2 && stream.getRemaining() < 2))
				return start;

			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();

			if (stream.getRemaining() < length) {
				return start;
			}

			byte[] data = new byte[length];
			stream.readBytes(data);
			try {
				queuePacket(packet, new InputStream(data));
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
		return stream.getOffset();
	}

	public void queuePacket(ClientPacket packet, InputStream stream) {
		Packet decoder = PACKET_DECODERS.get(packet);
		if (decoder != null)
			session.queuePacket(decoder.decodeAndCreateInstance(stream).setOpcode(packet));
	}

}
