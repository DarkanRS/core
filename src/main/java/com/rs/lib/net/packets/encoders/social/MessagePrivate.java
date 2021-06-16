package com.rs.lib.net.packets.encoders.social;

import com.rs.cache.Cache;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MessagePrivate extends PacketEncoder {
	
	private String username;
	private String message;

	public MessagePrivate(String username, String message) {
		super(ServerPacket.SEND_PRIVATE_MESSAGE);
		this.username = username;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(username);
		Cache.STORE.getHuffman().sendEncryptMessage(stream, message);
	}

}
