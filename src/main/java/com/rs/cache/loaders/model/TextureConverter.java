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
package com.rs.cache.loaders.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.cache.loaders.SpriteContainer;
import com.rs.cache.loaders.TextureDefinitions;
import com.rs.lib.io.OutputStream;

public class TextureConverter {

	private static Map<Integer, Integer> CONVERTED_MAP = new HashMap<>();

	public static int convert(Store from, Store to, int textureId, boolean quantize) throws IOException {
		if (CONVERTED_MAP.get(textureId) != null)
			return CONVERTED_MAP.get(textureId);
		if (textureId <= 2000)
			return textureId;
		RS3Tex tex = new RS3Tex(textureId);
		tex.decode(from, quantize);
		int newSpriteId = to.getIndex(IndexType.SPRITES).getLastArchiveId() + 1;
		SpriteContainer sprite = new SpriteContainer(tex.getImage());
		to.getIndex(IndexType.SPRITES).putFile(newSpriteId, 0, sprite.encode());

		OutputStream buffer = new OutputStream();
//		buffer.writeByte(3); // 3 operations
//		buffer.writeByte(0); // client doesnt read this byte
//
//		buffer.writeByte(39); // sprite operation opcode
//		buffer.writeByte(1);
//		buffer.writeByte(1);
//		buffer.writeByte(0);
//		buffer.writeShort(newSpriteId); // sprite id
//
//		buffer.writeBytes(new byte[] { 1, 0, 1, 0, 2, 0, 1, 1, 0, 0, 0, 1, 2, 0, 0, 1, 1, 0, 0, 0 }); // default operations
//		buffer.writeByte(34);
//		buffer.writeByte(0);
//		buffer.writeByte(0);
		
		buffer.writeByte(1);
		buffer.writeByte(0);
		buffer.writeByte(39);
		buffer.writeByte(1);
		buffer.writeByte(1);
		buffer.writeByte(0);
		buffer.writeShort(newSpriteId);
		buffer.writeByte(0);
		buffer.writeByte(0);
		buffer.writeByte(0);
		
		int newTex = to.getIndex(IndexType.MATERIALS).getLastArchiveId() + 1;
		if (to.getIndex(IndexType.MATERIALS).putFile(newTex, 0, buffer.toByteArray()))
			CONVERTED_MAP.put(textureId, newTex);
		
		TextureDefinitions def = TextureDefinitions.getRS3(newTex, from.getIndex(IndexType.TEXTURES).getFile(0, textureId));
		TextureDefinitions.addDef(def);
		TextureDefinitions.encodeAndReplace();
		System.out.println("Converted texture " + textureId + " to sprite: " + newSpriteId + " and texture: " + newTex);
		return newTex;
	}
}
