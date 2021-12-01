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

public class HitsplatDefinitions {
	
	private static final HashMap<Integer, HitsplatDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public int anInt2849 = -1;
	public int anInt2844 = 16777215;
	public boolean aBool2838 = false;
	int anInt2842 = -1;
	int anInt2851 = -1;
	int anInt2843 = -1;
	int anInt2845 = -1;
	public int anInt2846 = 0;
	String aString2840 = "";
	public int anInt2841 = 70;
	public int anInt2833 = 0;
	public int anInt2847 = -1;
	public int anInt2839 = -1;
	public int anInt2832 = 0;
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.HITSPLATS.getId())+1;i++) {
			HitsplatDefinitions defs = getDefs(i);
			System.out.println(defs);
		}
	}
	
	public static final HitsplatDefinitions getDefs(int id) {
		HitsplatDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.HITSPLATS.getId(), id);
		defs = new HitsplatDefinitions();
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
			this.anInt2849 = stream.readBigSmart();
		} else if (opcode == 2) {
			this.anInt2844 = stream.read24BitUnsignedInteger();
			this.aBool2838 = true;
		} else if (opcode == 3) {
			this.anInt2842 = stream.readBigSmart();
		} else if (opcode == 4) {
			this.anInt2851 = stream.readBigSmart();
		} else if (opcode == 5) {
			this.anInt2843 = stream.readBigSmart();
		} else if (opcode == 6) {
			this.anInt2845 = stream.readBigSmart();
		} else if (opcode == 7) {
			this.anInt2846 = stream.readShort();
		} else if (opcode == 8) { //TODO check readGJString compare to readJagString
			this.aString2840 = stream.readGJString();
		} else if (opcode == 9) {
			this.anInt2841 = stream.readUnsignedShort();
		} else if (opcode == 10) {
			this.anInt2833 = stream.readShort();
		} else if (opcode == 11) {
			this.anInt2847 = 0;
		} else if (opcode == 12) {
			this.anInt2839 = stream.readUnsignedByte();
		} else if (opcode == 13) {
			this.anInt2832 = stream.readShort();
		} else if (opcode == 14) {
			this.anInt2847 = stream.readUnsignedShort();
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
