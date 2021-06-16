package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class QuickChatPrivateEcho extends PacketEncoder {
	
	private String name;
	private int rights;
	private QuickChatMessage message;

	public QuickChatPrivateEcho(String name, int rights, QuickChatMessage message) {
		super(ServerPacket.MESSAGE_QUICKCHAT_PRIVATE_ECHO);
		this.name = name;
		this.rights = rights;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(/*!name.equals(display) ? 1 : 0*/ 0);
		stream.writeString(name);
//		if (!name.equals(display))
//			stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.random(255));
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getData() != null)
			stream.writeBytes(message.getData());
	}

}
