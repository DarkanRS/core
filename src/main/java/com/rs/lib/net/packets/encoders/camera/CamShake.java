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
package com.rs.lib.net.packets.encoders.camera;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CamShake extends PacketEncoder {
	
	//slot seems to work like this:
	//0 = camX addition
	//1 = camZ addition
	//2 = camY addition
	
	private int slotId;
	private int v1, v2, v3, v4;

	public CamShake(int slotId, int v1, int v2, int v3, int v4) {
		super(ServerPacket.CAM_SHAKE);
		this.slotId = slotId;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.write128Byte(v2);
		stream.writeShort(v4);
		stream.writeByteC(slotId);
		stream.writeByteC(v1);
		stream.write128Byte(v3);
	}

}
