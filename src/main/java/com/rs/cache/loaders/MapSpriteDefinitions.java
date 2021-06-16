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
