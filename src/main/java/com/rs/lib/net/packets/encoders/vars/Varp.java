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
package com.rs.lib.net.packets.encoders.vars;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class Varp extends PacketEncoder {
	
	private int id;
	private int value;

	public Varp(int id, int value) {
		super((value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) ? ServerPacket.VARP_LARGE : ServerPacket.VARP_SMALL);
		this.id = id;
		this.value = value;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			stream.writeIntV2(value);
			stream.writeShortLE128(id);
		} else {
			stream.writeByte(value);
			stream.writeShortLE128(id);
		}
	}

}
