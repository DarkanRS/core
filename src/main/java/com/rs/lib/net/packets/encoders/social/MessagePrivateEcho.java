package com.rs.lib.net.packets.encoders.social;

import com.rs.cache.Cache;
import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Account;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class MessagePrivateEcho extends PacketEncoder {
	
	private Account account;
	private String message;

	public MessagePrivateEcho(Account account, String message) {
		super(ServerPacket.MESSAGE_PRIVATE_ECHO);
		this.account = account;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(1); //Displayname true
		stream.writeString(account.getDisplayName());
		stream.writeString(account.getUsername());
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandomInclusive(255));
		stream.writeByte(account.getRights().getCrown());
		Cache.STORE.getHuffman().sendEncryptMessage(stream, message);
	}

}
