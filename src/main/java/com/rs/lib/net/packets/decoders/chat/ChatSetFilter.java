package com.rs.lib.net.packets.decoders.chat;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CHAT_SETFILTER)
public class ChatSetFilter extends Packet {

	private int publicFilter;
	private int privateFilter;
	private int tradeFilter;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ChatSetFilter packet = new ChatSetFilter();
		packet.publicFilter = stream.readByte();
		packet.privateFilter = stream.readByte();
		packet.tradeFilter = stream.readByte();
		return packet;
	}

	public int getPublicFilter() {
		return publicFilter;
	}

	public int getPrivateFilter() {
		return privateFilter;
	}

	public int getTradeFilter() {
		return tradeFilter;
	}

}
