package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class AddIgnore extends PacketEncoder {

	private String name;
	
	public AddIgnore(String name) {
		super(ServerPacket.ADD_IGNORE);
		this.name = name;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (name != null) {
			stream.writeByte(0x2);
			stream.writeString(name);
			stream.writeString(""); //updated name
		}
	}

}
