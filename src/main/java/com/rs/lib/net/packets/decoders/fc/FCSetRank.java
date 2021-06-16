package com.rs.lib.net.packets.decoders.fc;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.FC_SET_RANK)
public class FCSetRank extends Packet {

	private String name;
	private int rank;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		FCSetRank p = new FCSetRank();
		p.rank = stream.readUnsigned128Byte();
		p.name = stream.readString();
		return p;
	}

	public String getName() {
		return name;
	}

	public int getRank() {
		return rank;
	}

}
