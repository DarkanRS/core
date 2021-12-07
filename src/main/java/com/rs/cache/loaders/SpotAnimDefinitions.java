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
package com.rs.cache.loaders;

import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;

public class SpotAnimDefinitions {

	public boolean aBool6968;
	public int anInt6969;
	public int defaultModel;
	public int anInt6971;
	public short[] aShortArray6972;
	public short[] aShortArray6974;
	public short[] aShortArray6975;
	public int anInt6976;
	public int animationId = -1;
	public int anInt6978;
	public int anInt6979;
	public int anInt6980;
	public int anInt6981;
	public byte aByte6982;
	public short[] aShortArray6983;
	public int id;

	private static final ConcurrentHashMap<Integer, SpotAnimDefinitions> SPOT_ANIM_DEFS = new ConcurrentHashMap<Integer, SpotAnimDefinitions>();

	public static final SpotAnimDefinitions getDefs(int spotId) {
		SpotAnimDefinitions defs = SPOT_ANIM_DEFS.get(spotId);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.SPOT_ANIMS).getFile(ArchiveType.SPOT_ANIMS.archiveId(spotId), ArchiveType.SPOT_ANIMS.fileId(spotId));
		defs = new SpotAnimDefinitions();
		defs.id = spotId;
		if (data != null)
			defs.readValueLoop(new InputStream(data));
		SPOT_ANIM_DEFS.put(spotId, defs);
		return defs;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public void readValues(InputStream stream, int i) {
		if (i == 1)
			defaultModel = stream.readBigSmart();
		else if (2 == i)
			animationId = stream.readBigSmart();
		else if (i == 4)
			anInt6976 = stream.readUnsignedShort();
		else if (5 == i)
			anInt6971 = stream.readUnsignedShort();
		else if (6 == i)
			anInt6978 = stream.readUnsignedShort();
		else if (i == 7)
			anInt6979 = stream.readUnsignedByte();
		else if (i == 8)
			anInt6981 = stream.readUnsignedByte();
		else if (i == 9) {
			aByte6982 = (byte) 3;
			anInt6980 = 8224;
		} else if (i == 10)
			aBool6968 = true;
		else if (i == 11)
			aByte6982 = (byte) 1;
		else if (12 == i)
			aByte6982 = (byte) 4;
		else if (i == 13)
			aByte6982 = (byte) 5;
		else if (i == 14) {
			aByte6982 = (byte) 2;
			anInt6980 = stream.readUnsignedByte();
		} else if (15 == i) {
			aByte6982 = (byte) 3;
			anInt6980 = stream.readUnsignedShort();
		} else if (16 == i) {
			aByte6982 = (byte) 3;
			anInt6980 = stream.readInt();
		} else if (40 == i) {
			int i_3_ = stream.readUnsignedByte();
			aShortArray6972 = new short[i_3_];
			aShortArray6983 = new short[i_3_];
			for (int i_4_ = 0; i_4_ < i_3_; i_4_++) {
				aShortArray6972[i_4_] = (short) stream.readUnsignedShort();
				aShortArray6983[i_4_] = (short) stream.readUnsignedShort();
			}
		} else if (41 == i) {
			int i_5_ = stream.readUnsignedByte();
			aShortArray6974 = new short[i_5_];
			aShortArray6975 = new short[i_5_];
			for (int i_6_ = 0; i_6_ < i_5_; i_6_++) {
				aShortArray6974[i_6_] = (short) stream.readUnsignedShort();
				aShortArray6975[i_6_] = (short) stream.readUnsignedShort();
			}
		}
	}

	public SpotAnimDefinitions() {
		anInt6976 = 128;
		anInt6971 = 128;
		anInt6978 = 0;
		anInt6979 = 0;
		anInt6981 = 0;
		aBool6968 = false;
		aByte6982 = (byte) 0;
		anInt6980 = -1;
	}

}
