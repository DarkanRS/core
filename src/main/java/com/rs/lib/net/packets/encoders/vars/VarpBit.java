package com.rs.lib.net.packets.encoders.vars;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class VarpBit extends PacketEncoder {
	
	private int id;
	private int value;

	public VarpBit(int id, int value) {
		super((value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) ? ServerPacket.VARBIT_LARGE : ServerPacket.VARBIT_SMALL);
		this.id = id;
		this.value = value;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			stream.writeIntV2(value);
			stream.writeShort128(id);
		} else {
			stream.writeShort(id);
			stream.writeByte128(value);
		}
	}

}
