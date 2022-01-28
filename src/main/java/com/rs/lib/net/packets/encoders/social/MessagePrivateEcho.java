// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
		stream.writeDisplayNameChat(account);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandomInclusive(255));
		stream.writeByte(account.getRights().getCrown());
		Cache.STORE.getHuffman().sendEncryptMessage(stream, message);
	}

}
