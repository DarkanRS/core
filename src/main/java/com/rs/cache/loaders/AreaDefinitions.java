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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public final class AreaDefinitions {

	private static final ConcurrentHashMap<Integer, AreaDefinitions> defs = new ConcurrentHashMap<Integer, AreaDefinitions>();

	public int anInt2715;
	public int[] anIntArray2717;
	public int anInt2718;
	public int spriteId = -1;
	public int anInt2720;
	public int anInt2721;
	public int anInt2722;
	public int anInt2726;
	public int anInt2727;
	public boolean aBool2728;
	public boolean aBool2729;
	public boolean aBool2730;
	public int anInt2731;
	public String aString2732;
	int anInt2733;
	int anInt2734;
	int anInt2735;
	int anInt2736;
	public int[] anIntArray2738;
	int anInt2739;
	public String[] options;
	int anInt2741;
	public boolean aBool2742;
	int anInt2743;
	public int anInt2744;
	int anInt2745;
	public int anInt2746;
	public int anInt2747;
	public int anInt2748;
	public int anInt2749;
	public int anInt2750;
	public String areaName;
	public int anInt2752;
	public int anInt2753;
	public byte[] aByteArray2754;
	int anInt2755;
	public int anInt2756;
	public int anInt2757 = -1;
	public HashMap<Integer, Object> clientScriptMap = new HashMap<Integer, Object>();
	public int id;

	public static final AreaDefinitions getDefinitions(int id) {
		AreaDefinitions area = defs.get(id);
		if (area != null)
			return area;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.MAP_AREAS.getId(), id);
		area = new AreaDefinitions();
		area.id = id;
		if (data != null)
			area.readValueLoop(new InputStream(data));
		defs.put(id, area);
		return area;

	}
	
	public static void dump() throws IOException {
		File file = new File("worldMap.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));;
		writer.append("//Version = 727\n");
		writer.flush();
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.MAP_AREAS.getId())+1;i++) {
			AreaDefinitions defs = getDefinitions(i);
			writer.append(i+" - "+defs);
			writer.newLine();
			writer.flush();
		}
		writer.close();
	}

	AreaDefinitions() {
		options = new String[5];
		anInt2722 = 0;
		aBool2728 = true;
		aBool2729 = false;
		aBool2730 = true;
		aBool2742 = true;
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
		if (1 == i)
			spriteId = stream.readBigSmart();
		else if (2 == i)
			anInt2757 = stream.readBigSmart();
		else if (i == 3)
			areaName = stream.readString();
		else if (4 == i)
			anInt2720 = stream.read24BitInt();
		else if (5 == i)
			anInt2721 = stream.read24BitInt();
		else if (i == 6)
			anInt2722 = stream.readUnsignedByte();
		else if (i == 7) {
			int i_3_ = stream.readUnsignedByte();
			if ((i_3_ & 0x1) == 0)
				aBool2728 = false;
			if (2 == (i_3_ & 0x2))
				aBool2729 = true;
		} else if (i == 8)
			aBool2730 = stream.readUnsignedByte() == 1;
		else if (i == 9) {
			anInt2736 = stream.readUnsignedShort();
			if (anInt2736 == 65535)
				anInt2736 = -1;
			anInt2745 = stream.readUnsignedShort();
			if (anInt2745 == 65535)
				anInt2745 = 1;
			anInt2734 = stream.readInt();
			anInt2735 = stream.readInt();
		} else if (i >= 10 && i <= 14)
			options[i - 10] = stream.readString();
		else if (15 == i) {
			int i_4_ = stream.readUnsignedByte();
			anIntArray2717 = new int[2 * i_4_];
			for (int i_5_ = 0; i_5_ < i_4_ * 2; i_5_++)
				anIntArray2717[i_5_] = stream.readShort();
			anInt2715 = stream.readInt();
			int i_6_ = stream.readUnsignedByte();
			anIntArray2738 = new int[i_6_];
			for (int i_7_ = 0; i_7_ < anIntArray2738.length; i_7_++)
				anIntArray2738[i_7_] = stream.readInt();
			aByteArray2754 = new byte[i_4_];
			for (int i_8_ = 0; i_8_ < i_4_; i_8_++)
				aByteArray2754[i_8_] = (byte) stream.readByte();
		} else if (i == 16)
			aBool2742 = false;
		else if (17 == i)
			aString2732 = stream.readString();
		else if (i == 18)
			anInt2733 = stream.readBigSmart();
		else if (19 == i)
			anInt2718 = stream.readUnsignedShort();
		else if (20 == i) {
			anInt2755 = stream.readUnsignedShort();
			if (65535 == anInt2755)
				anInt2755 = 1;
			anInt2741 = stream.readUnsignedShort();
			if (anInt2741 == 65535)
				anInt2741 = 1;
			anInt2743 = stream.readInt();
			anInt2739 = stream.readInt();
		} else if (i == 21)
			anInt2727 = stream.readInt();
		else if (22 == i)
			anInt2726 = stream.readInt();
		else if (23 == i) {
			anInt2748 = stream.readUnsignedByte();
			anInt2749 = stream.readUnsignedByte();
			anInt2756 = stream.readUnsignedByte();
		} else if (24 == i) {
			anInt2750 = stream.readShort();
			anInt2752 = stream.readShort();
		} else if (i == 249) {
			int i_19_ = stream.readUnsignedByte();
			for (int i_21_ = 0; i_21_ < i_19_; i_21_++) {
				boolean bool = stream.readUnsignedByte() == 1;
				int i_22_ = stream.read24BitInt();
				if (bool)
					clientScriptMap.put(i_22_,  stream.readString());
				else
					clientScriptMap.put(i_22_,  stream.readInt());
			}
		}
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

}
