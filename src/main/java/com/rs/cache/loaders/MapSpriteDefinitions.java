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
package com.rs.cache.loaders;

import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;

public final class MapSpriteDefinitions {

	private static final ConcurrentHashMap<Integer, MapSpriteDefinitions> defs = new ConcurrentHashMap<Integer, MapSpriteDefinitions>();

	public boolean aBool7726 = false;
	public int anInt7727;
	public int spriteId;
	int[] anIntArray7730;
	public int id;

	public static final MapSpriteDefinitions getMapSpriteDefinitions(int id) {
		MapSpriteDefinitions script = defs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.MAP_SPRITES.getId(), id);
		script = new MapSpriteDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		defs.put(id, script);
		return script;

	}

	private MapSpriteDefinitions() {

	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int i) {
		if (i == 1)
			spriteId = stream.readBigSmart();
		else if (2 == i)
			anInt7727 = stream.read24BitInt();
		else if (i == 3)
			aBool7726 = true;
		else if (4 == i)
			spriteId = -1;
	}

}
