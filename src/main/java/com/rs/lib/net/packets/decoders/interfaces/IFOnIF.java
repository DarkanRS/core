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
package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.IF_ON_IF)
public class IFOnIF extends Packet {

	private int fromInter;
	private int toInter;
	private int fromComp;
	private int toComp;
	private int fromSlot;
	private int toSlot;
	private int fromItemId;
	private int toItemId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFOnIF p = new IFOnIF();
		p.toSlot = stream.readShortLE128();
		p.fromSlot = stream.readShortLE();
		p.toItemId = stream.readShortLE128();
		int hash2 = stream.readIntLE();
		int hash1 = stream.readIntV2();
		p.fromItemId = stream.readShortLE();
		p.fromInter = hash1 >> 16;
		p.toInter = hash2 >> 16;
		p.fromComp = hash1 & 0xFFFF;
		p.toComp = hash2 & 0xFFFF;
		return p;
	}
	
	public int getFromInter() {
		return fromInter;
	}

	public int getToInter() {
		return toInter;
	}

	public int getFromComp() {
		return fromComp;
	}

	public int getToComp() {
		return toComp;
	}

	public int getFromSlot() {
		return fromSlot;
	}

	public int getToSlot() {
		return toSlot;
	}

	public int getFromItemId() {
		return fromItemId;
	}

	public int getToItemId() {
		return toItemId;
	}

}
