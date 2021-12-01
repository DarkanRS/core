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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class BASDefinitions {
	
	public int numStandAnimations;
	public int[] standAnimations;
	public int standAnimation = -1;
	public int standTurn1;
	public int standTurn2;
	
	public int walkAnimation;
	public int walkDir1;
	public int walkDir2;
	public int walkDir3;
	public int walkTurn1;
	public int walkTurn2;
	
	public int runningAnimation;
	public int runDir1;
	public int runDir2;
	public int runDir3;
	public int runTurn1;
	public int runTurn2;
	
	public int teleportingAnimation;
	public int teleDir1;
	public int teleDir2;
	public int teleDir3;
	public int teleTurn1;
	public int teleTurn2;
	
	public int anInt2790;
	public int[][] anIntArrayArray2791;
	public int anInt2798;
	public int[][] anIntArrayArray2802;
	public int anInt2804;
	public int[] anIntArray2811;
	public int anInt2813;
	public int[] anIntArray2814;
	public int anInt2815;
	public int anInt2816;
	public int[] anIntArray2818;
	public int anInt2820;
	public int anInt2823;
	public int anInt2824;
	public int anInt2825;
	public int anInt2826;
	public int anInt2827;
	public int anInt2829;
	public int anInt2786;
	public boolean aBool2787;
	
	private static final ConcurrentHashMap<Integer, BASDefinitions> RENDER_ANIM_CACHE = new ConcurrentHashMap<Integer, BASDefinitions>();
	
	public static void main(String[] args) throws IOException {
		Cache.init("../darkan-cache/");
		int search = 772;
		for (int i = 0;i < Utils.getBASAnimDefSize();i++) {
			BASDefinitions def = BASDefinitions.getDefs(i);
			if (def.walkAnimation == search || def.runningAnimation == search || def.walkDir1 == search || def.walkDir2 == search || def.walkDir3 == search || def.standAnimation == search)
				System.out.println(i);
		}
	}

	public static final BASDefinitions getDefs(int emoteId) {
		BASDefinitions defs = RENDER_ANIM_CACHE.get(emoteId);
		if (defs != null)
			return defs;
		if (emoteId == -1)
			return null;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.BAS.getId(), emoteId);
		defs = new BASDefinitions();
		if (data != null)
			defs.readValueLoop(new InputStream(data));
		RENDER_ANIM_CACHE.put(emoteId, defs);
		return defs;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			result.append("  ");
			try {
				result.append(field.getType().getCanonicalName() + " " + field.getName() + ": ");
				result.append(Utils.getFieldValue(this, field));
			} catch (Throwable ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
	
	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (1 == opcode) {
			standAnimation = stream.readBigSmart();
			walkAnimation = stream.readBigSmart();
		} else if (2 == opcode)
			teleportingAnimation = stream.readBigSmart();
		else if (opcode == 3)
			teleDir3 = stream.readBigSmart();
		else if (opcode == 4)
			teleDir2 = stream.readBigSmart();
		else if (5 == opcode)
			teleDir1 = stream.readBigSmart();
		else if (opcode == 6)
			runningAnimation = stream.readBigSmart();
		else if (opcode == 7)
			runDir3 = stream.readBigSmart();
		else if (8 == opcode)
			runDir2 = stream.readBigSmart();
		else if (opcode == 9)
			runDir1 = stream.readBigSmart();
		else if (26 == opcode) {
			anInt2786 = ((short) (stream.readUnsignedByte() * 4));
			anInt2829 = ((short) (stream.readUnsignedByte() * 4));
		} else if (opcode == 27) {
			if (anIntArrayArray2802 == null)
				anIntArrayArray2802 = new int[15][];
			int i_1_ = stream.readUnsignedByte();
			anIntArrayArray2802[i_1_] = new int[6];
			for (int i_2_ = 0; i_2_ < 6; i_2_++)
				anIntArrayArray2802[i_1_][i_2_] = stream.readShort();
		} else if (opcode == 28) {
			int i_3_ = stream.readUnsignedByte();
			anIntArray2811 = new int[i_3_];
			for (int i_4_ = 0; i_4_ < i_3_; i_4_++) {
				anIntArray2811[i_4_] = stream.readUnsignedByte();
				if (255 == anIntArray2811[i_4_])
					anIntArray2811[i_4_] = -1;
			}
		} else if (29 == opcode)
			anInt2820 = stream.readUnsignedByte();
		else if (30 == opcode)
			anInt2804 = stream.readUnsignedShort();
		else if (opcode == 31)
			anInt2825 = stream.readUnsignedByte();
		else if (32 == opcode)
			anInt2823 = stream.readUnsignedShort();
		else if (33 == opcode)
			anInt2824 = stream.readShort();
		else if (opcode == 34)
			anInt2816 = stream.readUnsignedByte();
		else if (opcode == 35)
			anInt2815 = stream.readUnsignedShort();
		else if (36 == opcode)
			anInt2827 = stream.readShort();
		else if (37 == opcode)
			anInt2826 = stream.readUnsignedByte();
		else if (38 == opcode)
			standTurn1 = stream.readBigSmart();
		else if (opcode == 39)
			standTurn2 = stream.readBigSmart();
		else if (opcode == 40)
			walkDir3 = stream.readBigSmart();
		else if (41 == opcode)
			walkDir2 = stream.readBigSmart();
		else if (42 == opcode)
			walkDir1 = stream.readBigSmart();
		else if (opcode == 43)
			stream.readUnsignedShort();
		else if (opcode == 44)
			stream.readUnsignedShort();
		else if (45 == opcode)
			anInt2798 = stream.readUnsignedShort();
		else if (46 == opcode)
			teleTurn1 = stream.readBigSmart();
		else if (opcode == 47)
			teleTurn2 = stream.readBigSmart();
		else if (opcode == 48)
			runTurn1 = stream.readBigSmart();
		else if (49 == opcode)
			runTurn2 = stream.readBigSmart();
		else if (opcode == 50)
			walkTurn1 = stream.readBigSmart();
		else if (opcode == 51)
			walkTurn2 = stream.readBigSmart();
		else if (52 == opcode) {
			int i_5_ = stream.readUnsignedByte();
			anIntArray2814 = new int[i_5_];
			standAnimations = new int[i_5_];
			for (int i_6_ = 0; i_6_ < i_5_; i_6_++) {
				anIntArray2814[i_6_] = stream.readBigSmart();
				int i_7_ = stream.readUnsignedByte();
				standAnimations[i_6_] = i_7_;
				numStandAnimations += i_7_;
			}
		} else if (53 == opcode)
			aBool2787 = false;
		else if (opcode == 54) {
			anInt2813 = (stream.readUnsignedByte() << 6);
			anInt2790 = (stream.readUnsignedByte() << 6);
		} else if (opcode == 55) {
			if (anIntArray2818 == null)
				anIntArray2818 = new int[15];
			int i_8_ = stream.readUnsignedByte();
			anIntArray2818[i_8_] = stream.readUnsignedShort();
		} else if (opcode == 56) {
			if (null == anIntArrayArray2791)
				anIntArrayArray2791 = new int[15][];
			int i_9_ = stream.readUnsignedByte();
			anIntArrayArray2791[i_9_] = new int[3];
			for (int i_10_ = 0; i_10_ < 3; i_10_++)
				anIntArrayArray2791[i_9_][i_10_] = stream.readShort();
		}
	}

	public BASDefinitions() {
		anIntArray2814 = null;
		standAnimations = null;
		numStandAnimations = 0;
		standTurn1 = -1;
		standTurn2 = -1;
		walkAnimation = -1;
		walkDir3 = -1;
		walkDir2 = -1;
		walkDir1 = -1;
		runningAnimation = -1;
		runDir3 = -1;
		runDir2 = -1;
		runDir1 = -1;
		teleportingAnimation = -1;
		teleDir3 = -1;
		teleDir2 = -1;
		teleDir1 = -1;
		teleTurn1 = -1;
		teleTurn2 = -1;
		runTurn1 = -1;
		runTurn2 = -1;
		walkTurn1 = -1;
		walkTurn2 = -1;
		anInt2786 = 0;
		anInt2829 = 0;
		anInt2813 = 0;
		anInt2790 = 0;
		anInt2820 = 0;
		anInt2804 = 0;
		anInt2825 = 0;
		anInt2823 = 0;
		anInt2824 = 0;
		anInt2816 = 0;
		anInt2815 = 0;
		anInt2827 = 0;
		anInt2826 = -1;
		anInt2798 = -1;
		aBool2787 = true;
	}

}
