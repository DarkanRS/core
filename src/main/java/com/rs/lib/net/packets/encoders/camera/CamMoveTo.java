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

public class CamMoveTo extends PacketEncoder {
	
	private int moveLocalX, moveLocalY;
	private int moveZ;
	private int speed1;
	private int speed2;

	public CamMoveTo(int moveLocalX, int moveLocalY, int moveZ, int speed1, int speed2) {
		super(ServerPacket.CAM_MOVETO);
		this.moveLocalX = moveLocalX;
		this.moveLocalY = moveLocalY;
		this.moveZ = moveZ;
		this.speed1 = speed1;
		this.speed2 = speed2;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(moveLocalY);
		stream.writeByte(moveLocalX);
		stream.write128Byte(speed1);
		stream.writeShortLE(moveZ >> 2);
		stream.writeByte128(speed2);
	}

}
