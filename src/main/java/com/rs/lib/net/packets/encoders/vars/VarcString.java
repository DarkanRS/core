package com.rs.lib.net.packets.encoders.vars;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class VarcString extends PacketEncoder {
	
	private int id;
	private String string;

	public VarcString(int id, String string) {
		super(string.length() > 253 ? ServerPacket.CLIENT_SETVARCSTR_LARGE : ServerPacket.CLIENT_SETVARCSTR_SMALL);
		this.id = id;
		this.string = string;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (string.length() > 253) {
			stream.writeShortLE(id);
			stream.writeString(string);
		} else {
			stream.writeString(string);
			stream.writeShortLE128(id);
		}
	}

}
