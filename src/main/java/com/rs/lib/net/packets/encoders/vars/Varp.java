package com.rs.lib.net.packets.encoders.vars;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class Varp extends PacketEncoder {
	
	private int id;
	private int value;

	public Varp(int id, int value) {
		super((value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) ? ServerPacket.VARP_LARGE : ServerPacket.VARP_SMALL);
		this.id = id;
		this.value = value;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			stream.writeIntV2(value);
			stream.writeShortLE128(id);
		} else {
			stream.writeByte(value);
			stream.writeShortLE128(id);
		}
	}

}
