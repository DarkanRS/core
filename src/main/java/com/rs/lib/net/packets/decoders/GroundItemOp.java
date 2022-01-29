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
package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.GROUND_ITEM_OP1, ClientPacket.GROUND_ITEM_OP2, ClientPacket.GROUND_ITEM_OP3, ClientPacket.GROUND_ITEM_OP4, ClientPacket.GROUND_ITEM_OP5, ClientPacket.GROUND_ITEM_EXAMINE })
public class GroundItemOp extends Packet {
	
	private int objectId;
	private int x, y;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		GroundItemOp packet = new GroundItemOp();
		packet.objectId = stream.readUnsignedShortLE128();
		packet.forceRun = stream.readByteC() == 1;
		packet.y = stream.readUnsignedShort();
		packet.x = stream.readUnsignedShort128();
		return packet;
	}
	
	public int getObjectId() {
		return objectId;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public boolean isForceRun() {
		return forceRun;
	}

}
