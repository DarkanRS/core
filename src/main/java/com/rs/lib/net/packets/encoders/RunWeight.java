package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class RunWeight extends PacketEncoder {
	
	private int weight;

	public RunWeight(int weight) {
		super(ServerPacket.PLAYER_WEIGHT);
		this.weight = weight;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(weight);
	}

}
