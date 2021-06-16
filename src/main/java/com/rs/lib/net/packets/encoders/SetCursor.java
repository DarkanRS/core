package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class SetCursor extends PacketEncoder {
	
	private String walkHereReplace;
	private int cursor;

	public SetCursor(String walkHereReplace, int cursor) {
		super(ServerPacket.SET_CURSOR);
		this.walkHereReplace = walkHereReplace;
		this.cursor = cursor;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(walkHereReplace);
		stream.writeShort(cursor);
	}

}
