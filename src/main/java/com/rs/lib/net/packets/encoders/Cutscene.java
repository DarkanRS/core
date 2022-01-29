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
package com.rs.lib.net.packets.encoders;

import com.rs.cache.loaders.cutscenes.CutsceneArea;
import com.rs.cache.loaders.cutscenes.CutsceneDefinitions;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.MapXTEAs;

public class Cutscene extends PacketEncoder {

	private int id;
	private byte[] appearanceBlock;
	
	public Cutscene(int id, byte[] appearanceBlock) {
		super(ServerPacket.CUTSCENE);
		this.id = id;
		this.appearanceBlock = appearanceBlock;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(id);
		CutsceneDefinitions def = CutsceneDefinitions.getDefs(id);
		stream.writeShort(def.areas.size());
		for (CutsceneArea area : def.areas) {
			int[] xteas = MapXTEAs.getMapKeys(area.mapBase.getRegionId());
			for (int i = 0; i < 4; i++) {
				stream.writeInt(xteas != null ? xteas[i] : 0);
			}
		}
		stream.writeByte(appearanceBlock.length);
		stream.writeBytes(appearanceBlock);
	}

}
