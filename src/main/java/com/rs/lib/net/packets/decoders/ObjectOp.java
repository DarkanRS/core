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
package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.OBJECT_OP1, ClientPacket.OBJECT_OP2, ClientPacket.OBJECT_OP3, ClientPacket.OBJECT_OP4, ClientPacket.OBJECT_OP5, ClientPacket.OBJECT_EXAMINE })
public class ObjectOp extends Packet {
	
	private int objectId;
	private int x, y;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ObjectOp p = new ObjectOp();
		p.y = stream.readUnsignedShort();
		p.x = stream.readUnsignedShort();
		p.objectId = stream.readInt();
		p.forceRun = stream.readUnsignedByte128() == 1;
		return p;
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
