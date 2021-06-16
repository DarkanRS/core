package com.rs.lib.net.packets.encoders.vars;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class Varc extends PacketEncoder {
	
	private int id;
	private int value;

	public Varc(int id, int value) {
		super((value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) ? ServerPacket.CLIENT_SETVARC_LARGE : ServerPacket.CLIENT_SETVARC_SMALL);
		this.id = id;
		this.value = value;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			stream.writeShortLE(id);
			stream.writeIntV2(value);
		} else {
			stream.write128Byte(value);
			stream.writeShortLE(id);
		}
	}

}
