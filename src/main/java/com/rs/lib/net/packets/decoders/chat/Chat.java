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
package com.rs.lib.net.packets.decoders.chat;

import com.rs.cache.Cache;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;
import com.rs.lib.util.Utils;

@PacketDecoder(ClientPacket.CHAT)
public class Chat extends Packet {
	
	private int type = -1;
	private int color;
	private int effect;
	private String message;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		Chat chat = new Chat();
		chat.color = Utils.clampI(stream.readUnsignedByte(), 0, 12);
		chat.effect = Utils.clampI(stream.readUnsignedByte(), 0, 5);
		chat.message = Utils.fixChatMessage(Cache.STORE.getHuffman().readEncryptedMessage(200, stream));
		return chat;
	}
	
	public int getColor() {
		return color;
	}

	public int getEffect() {
		return effect;
	}

	public String getMessage() {
		return message;
	}

	public int getType() {
		return type;
	}

	public Chat setType(int type) {
		this.type = type;
		return this;
	}
}
