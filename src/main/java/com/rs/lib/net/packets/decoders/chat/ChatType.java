package com.rs.lib.net.packets.decoders.chat;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CHAT_TYPE)
public class ChatType extends Packet {
	
	private int type;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ChatType packet = new ChatType();
		packet.type = stream.readUnsignedByte();
		return packet;
	}

	public int getType() {
		return type;
	}
}
