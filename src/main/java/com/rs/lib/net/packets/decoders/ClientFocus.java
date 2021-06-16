package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLIENT_FOCUS)
public class ClientFocus extends Packet {

	private boolean inFocus;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ClientFocus p = new ClientFocus();
		p.inFocus = stream.readByte() == 1;
		return p;
	}

	public boolean isInFocus() {
		return inFocus;
	}

}
