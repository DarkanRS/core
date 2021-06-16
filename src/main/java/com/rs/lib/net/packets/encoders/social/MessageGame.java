package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MessageGame extends PacketEncoder {
	
	private int type;
	private String message;
	private String targetDisplayName;

	public MessageGame(int type, String message, String targetDisplayName) {
		super(ServerPacket.GAME_MESSAGE);
		this.type = type;
		this.message = message;
		this.targetDisplayName = targetDisplayName;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int maskData = 0;
		if (message.length() >= 248)
			message = message.substring(0, 248);

		if (targetDisplayName != null) {
			maskData |= 0x1;
//			if (p.hasDisplayName())
//				maskData |= 0x2;
		}
		stream.writeSmart(type);
		stream.writeInt(0); //junk
		stream.writeByte(maskData);
		if ((maskData & 0x1) != 0) {
			stream.writeString(targetDisplayName);
//			if (p.hasDisplayName())
//				stream.writeString(Utils.formatPlayerNameForDisplay(p.getUsername()));
		}
		stream.writeString(message);
	}

}
