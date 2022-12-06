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

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.lib.game.WorldObject;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CustomizeObject extends PacketEncoder {
	
	private WorldObject object;
	private int[] modifiedModels, modifiedColors, modifiedTextures;

	public CustomizeObject(WorldObject object, int[] modifiedModels, int[] modifiedColors, int[] modifiedTextures) {
		super(ServerPacket.CUSTOMIZE_OBJECT);
		this.object = object;
		this.modifiedModels = modifiedModels;
		this.modifiedColors = modifiedColors;
		this.modifiedTextures = modifiedTextures;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		ObjectDefinitions defs = object.getDefinitions();
		if (defs == null)
			throw new Error("Cannot modify object without any definitions.");
		stream.writeInt(object.getId());
		stream.writeByte128((object.getType().id << 2) + (object.getRotation() & 0x3));
		int flags = 0;
		if (modifiedModels == null && modifiedColors == null && modifiedTextures == null)
			flags |= 0x1;
		if (modifiedModels != null)
			flags |= 0x2;
		if (modifiedColors != null)
			flags |= 0x4;
		if (modifiedTextures != null)
			flags |= 0x8;
		stream.writeByte(flags);
		stream.writeByte128((object.getTile().getXInChunk() << 4) | object.getTile().getYInChunk());
		
		if ((flags & 0x2) == 2) {
			int count = defs.getModels(object.getType()).length;
			for (int i = 0;i < count;i++) {
				stream.writeInt(modifiedModels[i]);
			}
		}
		
		if ((flags & 0x4) == 4) {
			int count = 0;
			if (null != defs.modifiedColors)
				count = defs.modifiedColors.length;
			for (int i = 0;i < count;i++) {
				stream.writeShort(modifiedColors[i]);
			}
		}
		
		if ((flags & 0x8) == 8) {
			int count = 0;
			if (defs.modifiedTextures != null)
				count = defs.modifiedTextures.length;
			for (int i = 0;i < count;i++) {
				stream.writeShort(modifiedTextures[i]);
			}
		}
	}

}
