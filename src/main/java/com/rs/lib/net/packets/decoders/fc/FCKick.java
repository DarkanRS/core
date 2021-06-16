package com.rs.lib.net.packets.decoders.fc;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.FC_KICK)
public class FCKick extends Packet {

	private String name;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		FCKick p = new FCKick();
		p.name = stream.readString();
		return p;
	}

	public String getName() {
		return name;
	}

}
