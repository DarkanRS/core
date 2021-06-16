package com.rs.cache.loaders.cutscenes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class CutsceneEntityMovement {
	int[] movementTypes;
	int[] movementCoordinates;

	CutsceneEntityMovement(InputStream buffer) {
		int len = buffer.readUnsignedSmart();
		this.movementTypes = new int[len];
		this.movementCoordinates = new int[len];
		for (int i = 0; i < len; i++) {
			this.movementTypes[i] = buffer.readUnsignedByte();
			int x = buffer.readUnsignedShort();
			int y = buffer.readUnsignedShort();
			this.movementCoordinates[i] = y + (x << 16);
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
