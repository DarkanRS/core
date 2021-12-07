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
import java.util.HashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class HitbarDefinitions {
	
	private static final HashMap<Integer, HitbarDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public int anInt2446 = 255;
	public int anInt2440 = 255;
	public int anInt2439 = -1;
	public int anInt2443 = 70;
	int anInt2444 = -1;
	int anInt2445 = -1;
	int anInt2441 = -1;
	int anInt2447 = -1;
	public int anInt2442 = 1;
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.HITBARS.getId())+1;i++) {
			HitbarDefinitions defs = getDefs(i);
			System.out.println(defs);
		}
	}
	
	public static final HitbarDefinitions getDefs(int id) {
		HitbarDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.HITBARS.getId(), id);
		defs = new HitbarDefinitions();
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

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			stream.readUnsignedShort();
		} else if (opcode == 2) {
			this.anInt2446 = stream.readUnsignedByte();
		} else if (opcode == 3) {
			this.anInt2440 = stream.readUnsignedByte();
		} else if (opcode == 4) {
			this.anInt2439 = 0;
		} else if (opcode == 5) {
			this.anInt2443 = stream.readUnsignedShort();
		} else if (opcode == 6) {
			stream.readUnsignedByte();
		} else if (opcode == 7) {
			this.anInt2444 = stream.readBigSmart();
		} else if (opcode == 8) {
			this.anInt2445 = stream.readBigSmart();
		} else if (opcode == 9) {
			this.anInt2441 = stream.readBigSmart();
		} else if (opcode == 10) {
			this.anInt2447 = stream.readBigSmart();
		} else if (opcode == 11) {
			this.anInt2439 = stream.readUnsignedShort();
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
