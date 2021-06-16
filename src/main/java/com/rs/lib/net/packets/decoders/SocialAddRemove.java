package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.ADD_FRIEND, ClientPacket.ADD_IGNORE, ClientPacket.REMOVE_FRIEND, ClientPacket.REMOVE_IGNORE })
public class SocialAddRemove extends Packet {
	
	private String name;
	private boolean temp;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		SocialAddRemove p = new SocialAddRemove();
		p.name = stream.readString();
		p.temp = stream.getRemaining() > 0 ? stream.readUnsignedByte() == 1 : false;
		return p;
	}

	public String getName() {
		return name;
	}

	public boolean isTemp() {
		return temp;
	}
}
