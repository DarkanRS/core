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

@PacketDecoder(ClientPacket.IF_ON_OBJECT)
public class IFOnObject extends Packet {
	
	private int interfaceId;
	private int componentId;
	private int slotId;
	private int itemId;
	private int objectId;
	private int x;
	private int y;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFOnObject p = new IFOnObject();
		p.x = stream.readShortLE128();
		p.forceRun = stream.read128Byte() == 1;
		p.objectId = stream.readIntV1();
		int interfaceHash = stream.readInt();
		p.itemId = stream.readShortLE();
		p.slotId = stream.readShort128();
		p.y = stream.readShortLE();
		p.interfaceId = interfaceHash >> 16;
		p.componentId = interfaceHash - (p.interfaceId << 16);
		return p;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}

	public int getComponentId() {
		return componentId;
	}

	public int getSlotId() {
		return slotId;
	}

	public int getItemId() {
		return itemId;
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
