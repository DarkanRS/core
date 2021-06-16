package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class FriendsChatChannel extends PacketEncoder {
	
	public FriendsChatChannel() {
		super(ServerPacket.FRIENDS_CHAT_CHANNEL);
	}

	@Override
	public void encodeBody(OutputStream stream) {
//		if (manager != null)
//			stream.writeBytes(manager.getDataBlock());
	}

}
