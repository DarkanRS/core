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
package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSubActivePlayer extends PacketEncoder {

	private int pid;
	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;
	
	public IFOpenSubActivePlayer(int topId, int topChildId, int subId, boolean overlay, int pid) {
		super(ServerPacket.IF_OPENSUB_ACTIVE_PLAYER);
		this.pid = pid;
		this.topId = topId;
		this.topChildId = topChildId;
		this.subId = subId;
		this.overlay = overlay;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		
		stream.writeShort128(pid);
        stream.writeIntLE(xteas[1]);
        stream.writeByteC(overlay ? 1 : 0);
        stream.writeInt(topUid);
        stream.writeIntLE(xteas[3]);
        stream.writeShortLE128(subId);
        stream.writeInt(xteas[2]);
        stream.writeInt(xteas[0]);
	}

}
