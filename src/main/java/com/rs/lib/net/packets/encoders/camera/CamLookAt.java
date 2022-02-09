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

public class CamLookAt extends PacketEncoder {
	
	private int viewLocalX, viewLocalY, viewZ;
	private int speed1, speed2;

	public CamLookAt(int viewLocalX, int viewLocalY, int viewZ, int speed1, int speed2) {
		super(ServerPacket.CAM_LOOKAT);
		this.viewLocalX = viewLocalX;
		this.viewLocalY = viewLocalY;
		this.viewZ = viewZ;
		this.speed1 = speed1;
		this.speed2 = speed2;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(viewLocalX);
		stream.writeShort128(viewZ >> 2);
		stream.writeByte128(viewLocalY);
		stream.writeByteC(speed1);
		stream.write128Byte(speed2);
	}

}
