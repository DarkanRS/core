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
package com.rs.lib.net.packets.encoders;

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class HintTrail extends PacketEncoder {

	private WorldTile start;
	private int index = 0;
	private int modelId;
	private int[] stepsX, stepsY;
	private int size;
	
	public HintTrail(WorldTile start, int modelId, int[] stepsX, int[] stepsY, int size) {
		super(ServerPacket.HINT_TRAIL);
		this.start = start;
		this.modelId = modelId;
		this.stepsX = stepsX;
		this.stepsY = stepsY;
		this.size = size;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(index); //0-8 multiple trails
		if (size == -1) {
			stream.writeBigSmart(-1);
			return;
		}
				
		stream.writeBigSmart(modelId);
		stream.writeUnsignedSmart(size+1); //add 1 to steps since routefinder doesn't include first step?
		stream.writeShort(start.getX());
		stream.writeShort(start.getY());
		stream.writeByte(0); //0 offset to make sure first step is added
		stream.writeByte(0); //^
		for (int i = size - 1; i >= 0; i--) {
			stream.writeByte(stepsX[i] - start.getX());
			stream.writeByte(stepsY[i] - start.getY());
		}
	}

}
