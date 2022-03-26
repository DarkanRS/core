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
package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetTargetParam extends PacketEncoder {
	
	private int interfaceHash;
	private int fromSlot;
	private int toSlot;
	private int targetParam;

	public IFSetTargetParam(int interfaceId, int componentId, int fromSlot, int toSlot, int targetParam) {
		super(ServerPacket.IF_SETTARGETPARAM);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.fromSlot = fromSlot;
		this.toSlot = toSlot;
		this.targetParam = targetParam;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntV1(interfaceHash);
		stream.writeShortLE128(targetParam);
		stream.writeShort(toSlot);
		stream.writeShort128(fromSlot);
	}

}
