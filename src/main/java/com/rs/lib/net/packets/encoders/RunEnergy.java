package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class RunEnergy extends PacketEncoder {
	
	private int energy;

	public RunEnergy(int energy) {
		super(ServerPacket.RUN_ENERGY);
		this.energy = energy;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(energy);
	}

}
