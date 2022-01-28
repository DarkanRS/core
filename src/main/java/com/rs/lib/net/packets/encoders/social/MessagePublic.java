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
import com.rs.lib.game.PublicChatMessage;
import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MessagePublic extends PacketEncoder {
	
	private int pid;
	private int messageIcon;
	private PublicChatMessage message;

	public MessagePublic(int pid, int messageIcon, PublicChatMessage message) {
		super(ServerPacket.MESSAGE_PUBLIC);
		this.pid = pid;
		this.messageIcon = messageIcon;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(pid);
		stream.writeShort(message.getEffects());
		stream.writeByte(messageIcon);
		String filtered = message.getMessage();
		if (message instanceof QuickChatMessage qc) {
			stream.writeShort(qc.getFileId());
			if (qc.getData() != null)
				stream.writeBytes(qc.getData());
		} else {
			byte[] chatStr = new byte[250];
			chatStr[0] = (byte) filtered.length();
			int offset = 1 + Cache.STORE.getHuffman().encryptMessage(1, filtered.length(), chatStr, 0, filtered.getBytes());
			stream.writeBytes(chatStr, 0, offset);
		}
	}

}
