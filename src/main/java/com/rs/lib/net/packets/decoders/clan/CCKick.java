package com.rs.lib.net.packets.decoders.clan;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLANCHANNEL_KICKUSER)
public class CCKick extends Packet {

	private boolean guest;
	private String name;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		CCKick p = new CCKick();
		p.guest = stream.readByte() == 1;
		stream.readUnsignedShort();
		p.name = stream.readString();
		return p;
	}

	public String getName() {
		return name;
	}

	public boolean isGuest() {
		return guest;
	}

}
