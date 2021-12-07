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

@PacketDecoder(ClientPacket.CHAT_SETFILTER)
public class ChatSetFilter extends Packet {

	private int publicFilter;
	private int privateFilter;
	private int tradeFilter;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ChatSetFilter packet = new ChatSetFilter();
		packet.publicFilter = stream.readByte();
		packet.privateFilter = stream.readByte();
		packet.tradeFilter = stream.readByte();
		return packet;
	}

	public int getPublicFilter() {
		return publicFilter;
	}

	public int getPrivateFilter() {
		return privateFilter;
	}

	public int getTradeFilter() {
		return tradeFilter;
	}

}
