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

import java.util.Map;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.DisplayNamePair;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IgnoreList extends PacketEncoder {
	
	private Map<String, DisplayNamePair> ignores;

	public IgnoreList(Map<String, DisplayNamePair> ignores) {
		super(ServerPacket.UPDATE_IGNORE_LIST);
		this.ignores = ignores;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(ignores.size());
		for (String username : ignores.keySet()) {
			if (username != null) {
				stream.writeString(ignores.get(username).getCurrent());
				stream.writeString(ignores.get(username).getPrev());
			}
		}
	}

}
