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

public class PlayerOption extends PacketEncoder {
	
	private String option;
	private int slot;
	private boolean top;
	private int cursor;

	public PlayerOption(String option, int slot, boolean top, int cursor) {
		super(ServerPacket.PLAYER_OPTION);
		this.option = option;
		this.slot = slot;
		this.top = top;
		this.cursor = cursor;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(option);
		stream.writeByte128(slot);
		stream.writeShortLE128(cursor);
		stream.writeByteC(top ? 1 : 0);
	}

}
