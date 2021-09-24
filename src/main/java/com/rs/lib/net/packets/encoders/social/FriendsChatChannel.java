package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class FriendsChatChannel extends PacketEncoder {
	
	private byte[] block;
	
	public FriendsChatChannel(byte[] block) {
		super(ServerPacket.FRIENDS_CHAT_CHANNEL);
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (block != null)
			stream.writeBytes(block);
	}

}
