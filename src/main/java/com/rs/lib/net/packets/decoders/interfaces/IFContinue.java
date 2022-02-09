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

@PacketDecoder(ClientPacket.IF_CONTINUE)
public class IFContinue extends Packet {
	
	private int interfaceId;
	private int componentId;
	private int slotId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFContinue p = new IFContinue();
		int interfaceHash = stream.readIntV1();
		stream.readShortLE128();
		p.interfaceId = interfaceHash >> 16;
		p.slotId = (interfaceHash & 0xFF);
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

}
