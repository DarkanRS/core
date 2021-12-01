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
package com.rs.cache.loaders.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;

public class WorldMapDefinitions {
	private static Map<Integer, WorldMapDefinitions> CACHE = new HashMap<>();

	public int anInt9542 = -1;
	public int anInt9539 = 12800;
	public int anInt9540;
	public int anInt9541 = 12800;
	public int anInt9535;
	public int id;
	public String staticElementsName;
	public String areaName;
	public int regionHash;
	public int anInt9538 = -1;
	public boolean aBool9543 = true;
	public RegionSize mapSize;
	public List<WorldMapRect> areaRects = new ArrayList<>();

	public WorldMapDefinitions(int fileId) {
		this.id = fileId;
	}
	
	public static void loadAreas() {
		int detailsArchive = Cache.STORE.getIndex(IndexType.MAP_AREAS).getArchiveId("details");
		int size = Cache.STORE.getIndex(IndexType.MAP_AREAS).getValidFilesCount(detailsArchive);
		for (int i = 0; i < size; i++) {
			byte[] data = Cache.STORE.getIndex(IndexType.MAP_AREAS).getFile(detailsArchive, i);
			if (data == null)
				continue;
			WorldMapDefinitions defs = new WorldMapDefinitions(i);
			defs.init();
			CACHE.put(i, defs);
		}
	}
	
	public void decode(byte[] data) {
		InputStream stream = new InputStream(data);
		this.staticElementsName = stream.readString();
		this.areaName = stream.readString();
		this.regionHash = stream.readInt();
		this.anInt9538 = stream.readInt();
		this.aBool9543 = stream.readUnsignedByte() == 1;
		this.anInt9542 = stream.readUnsignedByte();
		if (this.anInt9542 == 255)
			this.anInt9542 = 0;
		this.mapSize = RegionSize.values()[stream.readUnsignedByte()];
		int size = stream.readUnsignedByte();
		for (int i = 0; i < size; i++) {
			areaRects.add(new WorldMapRect(stream.readUnsignedByte(), stream.readUnsignedShort(), stream.readUnsignedShort(), stream.readUnsignedShort(), stream.readUnsignedShort(), stream.readUnsignedShort(), stream.readUnsignedShort(), stream.readUnsignedShort(), stream.readUnsignedShort()));
		}
	}
	
	public StaticElements getStaticElements() {
//		int staticElementsArchive = Cache.STORE.getIndex(IndexType.MAP_AREAS).getArchiveId(staticElementsName + "_staticelements");
//		if (archiveId == -1) {
//            return new StaticElements(0);
//        } else {
//            int[] fileIds = index_0.getValidFileIds(archiveId);
//            StaticElements elements = new StaticElements(fileIds.length);
//            int id = 0;
//            int fileIndex = 0;
//            while (true) {
//                while (id < elements.size) {
//                    ByteBuf buffer = new ByteBuf(index_0.getFile(archiveId, fileIds[fileIndex++]));
//                    int regionHash = buffer.readInt();
//                    int areaId = buffer.readUnsignedShort();
//                    int isMembers = buffer.readUnsignedByte();
//                    if (!members && isMembers == 1) {
//                        --elements.size;
//                    } else {
//                        elements.regionHashes[id] = regionHash;
//                        elements.areaIds[id] = areaId;
//                        ++id;
//                    }
//                }
//                return elements;
//            }
//        }
		return null;
	}

	public boolean method14775(int i_1, int i_2, int[] ints_3) {
		for (WorldMapRect rect : areaRects) {
			if (rect.method12409(i_1, i_2)) {
				rect.method12410(i_1, i_2, ints_3);
				return true;
			}
		}

		return false;
	}

	public boolean method14777(int i_1, int i_2, int[] ints_3) {
		for (WorldMapRect rect : areaRects) {
			if (rect.method12415(i_1, i_2)) {
				rect.method12414(i_1, i_2, ints_3);
				return true;
			}
		}

		return false;
	}

	public boolean method14778(int i_1, int i_2, int i_3, int[] ints_4) {
		for (WorldMapRect rect : areaRects) {
			if (rect.method12408(i_1, i_2, i_3)) {
				rect.method12414(i_2, i_3, ints_4);
				return true;
			}
		}

		return false;
	}

	public void init() {
		this.anInt9539 = 12800;
		this.anInt9540 = 0;
		this.anInt9541 = 12800;
		this.anInt9535 = 0;

		for (WorldMapRect rect : areaRects) {
			if (rect.bestBottomLeftX < anInt9539)
				this.anInt9539 = rect.bestBottomLeftX;
			if (rect.bestTopRightX > anInt9540)
				this.anInt9540 = rect.bestTopRightX;
			if (rect.bestBottomLeftY < anInt9541)
				this.anInt9541 = rect.bestBottomLeftY;
			if (rect.bestTopRightY > anInt9535)
				this.anInt9535 = rect.bestTopRightY;
		}

	}

	public boolean method14784(int i_1, int i_2) {
		for (WorldMapRect rect : areaRects) {
			if (rect.method12415(i_1, i_2)) {
				return true;
			}
		}

		return false;
	}
}
