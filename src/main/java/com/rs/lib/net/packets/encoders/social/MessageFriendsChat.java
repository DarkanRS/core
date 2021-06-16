package com.rs.lib.net.packets.encoders.social;

import com.rs.cache.Cache;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class MessageFriendsChat extends PacketEncoder {
	
	private String name;
	private int rights;
	private String chatName;
	private String message;

	public MessageFriendsChat(String name, int rights, String chatName, String message) {
		super(ServerPacket.MESSAGE_FRIENDS_CHAT);
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
			stream.writeByte(Utils.getRandomInclusive(255));
		stream.writeByte(rights);
		Cache.STORE.getHuffman().sendEncryptMessage(stream, message);
	}

}
