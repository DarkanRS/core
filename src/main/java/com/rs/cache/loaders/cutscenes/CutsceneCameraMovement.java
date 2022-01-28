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
package com.rs.cache.loaders.cutscenes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class CutsceneCameraMovement {
	int[] anIntArray763;
	int[] anIntArray760;
	int[] anIntArray762;
	int[] anIntArray759;
	int[] anIntArray761;
	int[] anIntArray764;
	int[] anIntArray765;

	CutsceneCameraMovement(InputStream buffer) {
		int len = buffer.readUnsignedSmart();
		this.anIntArray763 = new int[len];
		this.anIntArray760 = new int[len];
		this.anIntArray762 = new int[len];
		this.anIntArray759 = new int[len];
		this.anIntArray761 = new int[len];
		this.anIntArray764 = new int[len];
		this.anIntArray765 = new int[len];
		for (int i = 0; i < len; i++) {
			this.anIntArray763[i] = buffer.readUnsignedShort() - 5120;
			this.anIntArray762[i] = buffer.readUnsignedShort() - 5120;
			this.anIntArray760[i] = buffer.readShort();
			this.anIntArray761[i] = buffer.readUnsignedShort() - 5120;
			this.anIntArray765[i] = buffer.readUnsignedShort() - 5120;
			this.anIntArray764[i] = buffer.readShort();
			this.anIntArray759[i] = buffer.readShort();
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
