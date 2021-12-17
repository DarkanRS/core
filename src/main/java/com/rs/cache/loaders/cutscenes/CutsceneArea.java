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

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class CutsceneArea {
	public WorldTile mapBase;
	public int regionId;
	public int width;
	public int length;
	public int anInt7481;
	public int anInt7480;
	public int anInt7483;
	public int anInt7486;

	CutsceneArea(InputStream buffer) {
		int position = buffer.readInt();
		this.mapBase = new WorldTile(position >>> 14 & 0x3fff, position & 0x3fff, position >>> 28);
		this.width = buffer.readUnsignedByte();
		this.length = buffer.readUnsignedByte();
		this.anInt7481 = buffer.readUnsignedByte();
		this.anInt7480 = buffer.readUnsignedByte();
		this.anInt7483 = buffer.readUnsignedByte();
		this.anInt7486 = buffer.readUnsignedByte();
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
