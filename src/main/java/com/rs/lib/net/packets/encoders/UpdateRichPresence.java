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
package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateRichPresence extends PacketEncoder {
	
	private String fieldName;
	private Object value;

	public UpdateRichPresence(String fieldName, Object value) {
		super(ServerPacket.DISCORD_RICH_PRESENCE_UPDATE);
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(fieldName);
		if (value instanceof Integer i) {
			stream.writeInt(0);
			stream.writeInt(i);
		} else if (value instanceof String s) {
			stream.writeInt(1);
			stream.writeString(s);
		} else if (value instanceof Long l) {
			stream.writeInt(2);
			stream.writeLong(l);
		}
	}
}
