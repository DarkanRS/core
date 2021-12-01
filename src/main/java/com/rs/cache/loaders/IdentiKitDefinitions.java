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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class IdentiKitDefinitions {
	
	private static final HashMap<Integer, IdentiKitDefinitions> CACHE = new HashMap<>();
	
	public int id;
	int[] modelIds;
    short[] originalColours;
    short[] replacementColours;
    short[] originalTextures;
    short[] replacementTextures;
    int[] headModels = new int[] { -1, -1, -1, -1, -1 };
	
	public static final IdentiKitDefinitions getDefs(int id) {
		IdentiKitDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.IDENTIKIT.getId(), id);
		defs = new IdentiKitDefinitions();
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
    		buffer.readUnsignedByte();
    	} else if (opcode == 2) {
    		int count = buffer.readUnsignedByte();
    		this.modelIds = new int[count];
    		for (int i_5 = 0; i_5 < count; i_5++) {
    			this.modelIds[i_5] = buffer.readBigSmart();
    		}
    	} else if (opcode == 40) {
    		int count = buffer.readUnsignedByte();
    		this.originalColours = new short[count];
    		this.replacementColours = new short[count];
    		for (int i_5 = 0; i_5 < count; i_5++) {
    			this.originalColours[i_5] = (short) buffer.readUnsignedShort();
    			this.replacementColours[i_5] = (short) buffer.readUnsignedShort();
    		}
    	} else if (opcode == 41) {
    		int  count = buffer.readUnsignedByte();
    		this.originalTextures = new short[count];
    		this.replacementTextures = new short[count];
    		for (int i_5 = 0; i_5 < count; i_5++) {
    			this.originalTextures[i_5] = (short) buffer.readUnsignedShort();
    			this.replacementTextures[i_5] = (short) buffer.readUnsignedShort();
    		}
    	} else if (opcode >= 60 && opcode < 70) {
    		this.headModels[opcode - 60] = buffer.readBigSmart();
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
