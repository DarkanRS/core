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
package com.rs.lib.net.packets.encoders.zonespecific;

import com.rs.lib.game.Projectile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ProjAnimSpecific extends PacketEncoder {
	
	private Projectile proj;

	public ProjAnimSpecific(Projectile proj) {
		super(ServerPacket.PROJANIM_SPECIFIC);
		this.proj = proj;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int flags = 0;
		if (proj.usesTerrainHeight())
			flags |= 0x1;
		if (proj.getBASFrameHeightAdjust() != -1) {
			flags |= 0x2;
			flags += (proj.getBASFrameHeightAdjust() << 2);
			//startHeight will not be multiplied by 4 with this flag active?
		}
		stream.writeByte128(proj.getEndHeight());
		stream.writeShortLE128(proj.getSourceId());
		stream.writeShortLE(proj.getEndTime());
		stream.writeShort(proj.getLockOnId());
		stream.writeByte128(flags);
		stream.writeShort128(proj.getSource().getY());
		stream.writeShort(proj.getSlope());
		stream.writeShortLE128(proj.getStartTime());
		stream.writeShortLE128(proj.getSpotAnimId());
		stream.writeByte128(proj.getAngle());
		stream.writeByteC(proj.getDestination().getX() - proj.getSource().getX());
		stream.writeByteC(proj.getDestination().getY() - proj.getSource().getY());
		stream.writeShort128(proj.getSource().getX());
		stream.writeByteC(proj.getStartHeight());
	}

}
