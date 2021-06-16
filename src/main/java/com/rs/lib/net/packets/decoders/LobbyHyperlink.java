package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.LOBBY_HYPERLINK)
public class LobbyHyperlink extends Packet {
	
	private String service;
	private String page;
	private int flags;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		LobbyHyperlink p = new LobbyHyperlink();
		p.service = stream.readString();
		p.page = stream.readString();
		stream.readString();
		p.flags = stream.readByte();
		return p;
	}

	public String getService() {
		return service;
	}

	public String getPage() {
		return page;
	}

	public int getFlags() {
		return flags;
	}

}
