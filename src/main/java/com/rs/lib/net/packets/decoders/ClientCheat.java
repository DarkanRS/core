package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLIENT_CHEAT)
public class ClientCheat extends Packet {
	
	private boolean client;
	private String command;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ClientCheat p = new ClientCheat();
		p.client = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		boolean unknown = stream.readUnsignedByte() == 1;
		p.command = stream.readString();
		return p;
	}

	public String getCommand() {
		return command;
	}

	public boolean isClient() {
		return client;
	}

}
