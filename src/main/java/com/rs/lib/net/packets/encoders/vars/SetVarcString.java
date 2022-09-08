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

public class SetVarcString extends PacketEncoder {
	
	private int id;
	private String string;

	public SetVarcString(int id, String string) {
		super(string.length()+2 > Byte.MAX_VALUE ? ServerPacket.CLIENT_SETVARCSTR_LARGE : ServerPacket.CLIENT_SETVARCSTR_SMALL);
		this.id = id;
		this.string = string;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (string.length()+2 > Byte.MAX_VALUE) {
			stream.writeShortLE(id);
			stream.writeString(string);
		} else {
			stream.writeString(string);
			stream.writeShortLE128(id);
		}
	}

}
