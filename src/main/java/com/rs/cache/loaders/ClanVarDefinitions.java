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
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class ClanVarDefinitions {
	
	private static final HashMap<Integer, ClanVarDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public char aChar4832;
	public CS2Type type;
	public int baseVar = -1;
	public int startBit;
	public int endBit;
	
	public static void main(String[] args) throws IOException {
		Cache.init("../cache/");
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.CLAN_VAR.getId()) + 1;i++) {
			ClanVarDefinitions defs = getDefs(i);
			System.out.println(i + " - " + defs.baseVar + " - " + defs.type);
		}
	}
	
	public static final ClanVarDefinitions getDefs(int id) {
		ClanVarDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.CLAN_VAR.getId(), id);
		
		defs = new ClanVarDefinitions();
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
			this.aChar4832 = Utils.cp1252ToChar((byte) stream.readByte());
			this.type = CS2Type.forJagexDesc(this.aChar4832);
		} else if (opcode == 3) {
			this.baseVar = stream.readUnsignedShort();
			this.startBit = stream.readUnsignedByte();
			this.endBit = stream.readUnsignedByte();
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
