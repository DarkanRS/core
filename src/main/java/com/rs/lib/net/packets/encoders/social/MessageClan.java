package com.rs.lib.net.packets.encoders.social;

import com.rs.cache.Cache;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class MessageClan extends PacketEncoder {
	
	private String displayName;
	private int rights;
	private String message;
	private boolean guest;

	public MessageClan(String displayName, int rights, String message, boolean guest) {
		super(ServerPacket.MESSAGE_CLANCHANNEL);
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
		Cache.STORE.getHuffman().sendEncryptMessage(stream, message);
	}

}
