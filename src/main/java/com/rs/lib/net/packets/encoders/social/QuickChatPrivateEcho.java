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

import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Account;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class QuickChatPrivateEcho extends PacketEncoder {
	
	private Account account;
	private QuickChatMessage message;

	public QuickChatPrivateEcho(Account account, QuickChatMessage message) {
		super(ServerPacket.MESSAGE_QUICKCHAT_PRIVATE_ECHO);
		this.account = account;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeDisplayNameChat(account);
		stream.writeShort(0); //unk
		stream.write24BitInteger(0); //unk
		stream.writeByte(account.getRights().getCrown());
		stream.writeShort(message.getFileId());
		if (message.getData() != null)
			stream.writeBytes(message.getData());
	}

}
