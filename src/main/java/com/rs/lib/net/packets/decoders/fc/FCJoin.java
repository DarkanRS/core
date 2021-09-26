package com.rs.lib.net.packets.decoders.fc;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.FC_JOIN)
public class FCJoin extends Packet {

	private String name;
	
	//Required for reflection in decoder parsing
	public FCJoin() {
		
	}
	
	public FCJoin(String name) {
		this.name = name;
	}
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		FCJoin p = new FCJoin();
		if (stream.getRemaining() > 0)
			p.name = stream.readString();
		return p;
	}

	public String getName() {
		return name;
	}
	
}
