package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Account;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class AddIgnore extends PacketEncoder {

	private Account account;
	
	public AddIgnore(Account account) {
		super(ServerPacket.ADD_IGNORE);
		this.account = account;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(0x2);
		stream.writeString(account.getDisplayName());
		stream.writeString(account.getPrevDisplayName());
	}

}
