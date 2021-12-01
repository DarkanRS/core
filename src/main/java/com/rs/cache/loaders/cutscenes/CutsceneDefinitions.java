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
package com.rs.cache.loaders.cutscenes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class CutsceneDefinitions {
	
	private static final HashMap<Integer, CutsceneDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public int anInt825;
	public int anInt824;
	public List<CutsceneArea> areas = new ArrayList<>();
	public List<CutsceneCameraMovement> camMovements = new ArrayList<>();
	public List<CutsceneEntity> entities = new ArrayList<>();
	public List<CutsceneObject> objects = new ArrayList<>();
	public List<CutsceneEntityMovement> movements = new ArrayList<>();
	
	public static final CutsceneDefinitions getDefs(int id) {
		CutsceneDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		
		byte[] data = Cache.STORE.getIndex(IndexType.CUTSCENES).getFile(id);
		
		defs = new CutsceneDefinitions();
		defs.id = id;
		if (data != null)
			defs.decode(new InputStream(data));
		CACHE.put(id, defs);
		return defs;
	}
	
	private void decode(InputStream buffer) {
		readValues(buffer);
		int len = buffer.readUnsignedByte();
		for (int i = 0; i < len; i++)
			areas.add(new CutsceneArea(buffer));
		len = buffer.readUnsignedSmart();
		for (int i = 0; i < len; i++)
			camMovements.add(new CutsceneCameraMovement(buffer));
		len = buffer.readUnsignedSmart();
		for (int i = 0; i < len; i++)
			entities.add(new CutsceneEntity(buffer, i));
		len = buffer.readUnsignedSmart();
		for (int i = 0; i < len; i++)
			objects.add(new CutsceneObject(buffer));
		len = buffer.readUnsignedSmart();
		for (int i = 0; i < len; i++)
			movements.add(new CutsceneEntityMovement(buffer));
		buffer.skip(buffer.getRemaining());
		//TODO decode the other like.. 26 diff cutscene actions lmao
	}

	private void readValues(InputStream stream) {
		while (true) {
			int i = stream.readUnsignedByte();
			switch (i) {
			case 0:
				anInt825 = stream.readUnsignedShort();
				anInt824 = stream.readUnsignedShort();
				break;
			case 255:
				return;
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
