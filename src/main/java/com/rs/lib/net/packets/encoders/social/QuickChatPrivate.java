package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class QuickChatPrivate extends PacketEncoder {
	
	private String username;
	private QuickChatMessage message;

	public QuickChatPrivate(String username, QuickChatMessage message) {
		super(ServerPacket.MESSAGE_QUICKCHAT_PRIVATE);
		this.username = username;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(username);
		stream.writeShort(message.getFileId());
		if (message.getData() != null)
			stream.writeBytes(message.getData());
	}

}
