package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateRichPresence extends PacketEncoder {
	
	private String fieldName;
	private Object value;

	public UpdateRichPresence(String fieldName, Object value) {
		super(ServerPacket.DISCORD_RICH_PRESENCE_UPDATE);
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(fieldName);
		if (value instanceof Integer) {
			stream.writeInt(0);
			stream.writeInt((Integer) value);
		} else if (value instanceof String) {
			stream.writeInt(1);
			stream.writeString((String) value);
		} else if (value instanceof Long) {
			stream.writeInt(2);
			stream.writeLong((Long) value);
		}
	}
}
