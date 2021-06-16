package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class QuickChatFriendsChat extends PacketEncoder {
	
	private String name;
	private int rights;
	private String chatName;
	private QuickChatMessage message;

	public QuickChatFriendsChat(String name, int rights, String chatName, QuickChatMessage message) {
		super(ServerPacket.MESSAGE_QUICKCHAT_FRIENDS_CHAT);
		this.name = name;
		this.rights = rights;
		this.chatName = chatName;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(/*!name.equals(display) ? 1 : 0*/ 0);
		stream.writeString(name);
//		if (!name.equals(display))
//			stream.writeString(display);
		stream.writeLong(Utils.stringToLong(chatName));
		
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.random(255));
		//^ is in place of
		/* long l_160_ = stream.readUnsignedShort();
		 * long l_161_ = stream.read24BitUnsignedInteger();
		 * long l_164_ = (l_160_ << 32) + l_161_;
		 */
		
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getData() != null)
			stream.writeBytes(message.getData());
	}

}
