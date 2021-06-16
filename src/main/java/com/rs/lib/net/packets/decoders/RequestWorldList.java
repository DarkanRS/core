package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.REQUEST_WORLD_LIST)
public class RequestWorldList extends Packet {
	
	private int worldId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		RequestWorldList packet = new RequestWorldList();
		packet.worldId = stream.readInt();
		return packet;
	}
	
	public int getWorldId() {
		return worldId;
	}

}
