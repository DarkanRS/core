package com.rs.lib.net.packets.decoders;

import java.lang.reflect.Modifier;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.REFLECTION_CHECK)
public class ReflectionCheckResponse extends Packet {
	
	private int id;
	private boolean exists;
	private String modifiers;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ReflectionCheckResponse p = new ReflectionCheckResponse();
		p.id = stream.readInt();
		p.exists = stream.readByte() == 1;
		if (!p.exists)
			p.modifiers = Modifier.toString(stream.readInt());
		return p;
	}

	public int getId() {
		return id;
	}

	public String getModifiers() {
		return modifiers;
	}

	public boolean exists() {
		return exists;
	}

}
