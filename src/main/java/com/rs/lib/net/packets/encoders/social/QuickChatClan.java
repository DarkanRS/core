package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class QuickChatClan extends PacketEncoder {
	
	private String displayName;
	private int rights;
	private QuickChatMessage message;
	private boolean guest;

	public QuickChatClan(String displayName, int rights, QuickChatMessage message, boolean guest) {
		super(ServerPacket.MESSAGE_QUICKCHAT_CLANCHANNEL);
		this.displayName = displayName;
		this.rights = rights;
		this.message = message;
		this.guest = guest;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(guest ? 0 : 1);
		stream.writeString(displayName);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandomInclusive(255));
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getData() != null)
			stream.writeBytes(message.getData());
	}

}
