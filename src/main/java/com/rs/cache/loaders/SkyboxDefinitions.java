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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class SkyboxDefinitions {
	
	private static final HashMap<Integer, SkyboxDefinitions> CACHE = new HashMap<>();
	
	public int id;
	int anInt2653 = -1;
	int anInt2654 = -1;
	SkyboxRelatedEnum aClass204_2656;
	int anInt2657;
	int[] anIntArray2655;
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.SKYBOX.getId())+1;i++) {
			SkyboxDefinitions defs = getDefs(i);
			System.out.println(defs);
		}
	}
	
	public static final SkyboxDefinitions getDefs(int id) {
		SkyboxDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.SKYBOX.getId(), id);
		defs = new SkyboxDefinitions();
		defs.id = id;
		if (data != null)
			defs.readValueLoop(new InputStream(data));
		CACHE.put(id, defs);
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

	private void readValues(InputStream buffer, int opcode) {
		if (opcode == 1) {
			this.anInt2653 = buffer.readUnsignedShort();
		} else if (opcode == 2) {
			this.anIntArray2655 = new int[buffer.readUnsignedByte()];
			for (int i_4 = 0; i_4 < this.anIntArray2655.length; i_4++) {
				this.anIntArray2655[i_4] = buffer.readUnsignedShort();
			}
		} else if (opcode == 3) {
			this.anInt2654 = buffer.readUnsignedByte();
		} else if (opcode == 4) {
			this.aClass204_2656 = SkyboxRelatedEnum.values()[buffer.readUnsignedByte()];
		} else if (opcode == 5) {
			this.anInt2657 = buffer.readBigSmart();
		}
	}
	
	public enum SkyboxRelatedEnum {
		TYPE0,
		TYPE1
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
