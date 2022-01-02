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
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.net.packets.decoders.chat;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.QUICKCHAT_PUBLIC)
public class QCPublic extends Packet {
	
	private int chatType;
	private int qcId;
	private byte[] messageData;
	private byte[] completedData;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		QCPublic p = new QCPublic();
		p.chatType = stream.readByte();
		p.qcId = stream.readUnsignedShort();
		p.messageData = null;
		if (stream.getRemaining() > 0) {
			p.messageData = new byte[stream.getRemaining()];
			stream.readBytes(p.messageData);
		}
		return p;
	}

	public int getChatType() {
		return chatType;
	}

	public int getQcId() {
		return qcId;
	}

	public byte[] getMessageData() {
		return messageData;
	}

	public byte[] getCompletedData() {
		return completedData;
	}

	public void setCompletedData(byte[] completedData) {
		this.completedData = completedData;
	}

}
