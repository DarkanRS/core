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
package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.Projectile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ProjAnimHalfSq extends PacketEncoder {
	
	private Projectile proj;

	public ProjAnimHalfSq(Projectile proj) {
		super(ServerPacket.MAP_PROJANIM_HALFSQ);
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
		
		stream.writeByte((proj.getSource().getXInChunk() << 3) | proj.getSource().getYInChunk());
		stream.writeByte(flags);
		stream.writeByte(proj.getDestination().getX() - proj.getSource().getX());
		stream.writeByte(proj.getDestination().getY() - proj.getSource().getY());
		stream.writeShort(proj.getSourceId());
		stream.writeShort(proj.getLockOnId());
		stream.writeShort(proj.getSpotAnimId());
		stream.writeByte(proj.getStartHeight());
		stream.writeByte(proj.getEndHeight());
		stream.writeShort(proj.getStartTime());
		stream.writeShort(proj.getEndTime());
		stream.writeByte(proj.getAngle());
		stream.writeShort(proj.getSlope());
	}

}
