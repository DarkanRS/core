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

@PacketDecoder(ClientPacket.IF_DRAG_ONTO_IF)
public class IFDragOntoIF extends Packet {
	
	private int fromInter;
	private int toInter;
	private int fromComp;
	private int toComp;
	private int fromSlot;
	private int toSlot;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFDragOntoIF p = new IFDragOntoIF();
		p.toSlot = stream.readShortLE128();
		p.fromSlot = stream.readShortLE();
		stream.readShort();
		stream.readShortLE128();
		int fromInterfaceHash = stream.readIntV1();
		int toInterfaceHash = stream.readIntLE();

		p.toInter = toInterfaceHash >> 16;
		p.toComp = toInterfaceHash - (p.toInter << 16);
		p.fromInter = fromInterfaceHash >> 16;
		p.fromComp = fromInterfaceHash - (p.fromInter << 16);
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
}
