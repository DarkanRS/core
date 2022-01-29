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
package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSubActiveNPC extends PacketEncoder {

	private int npcSid;
	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;
	
	public IFOpenSubActiveNPC(int topId, int topChildId, int subId, boolean overlay, int npcSid) {
		super(ServerPacket.IF_OPENSUB_ACTIVE_NPC);
		this.npcSid = npcSid;
		this.topId = topId;
		this.topChildId = topChildId;
		this.subId = subId;
		this.overlay = overlay;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		
		stream.writeIntLE(xteas[3]);
        stream.writeByte(overlay ? 1 : 0);
        stream.writeIntV1(xteas[0]);
        stream.writeShort(subId);
        stream.writeInt(xteas[1]);
        stream.writeIntV1(topUid);
        stream.writeIntV1(xteas[2]);
        stream.writeShortLE(npcSid);
	}

}
