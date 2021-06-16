package com.rs.cache.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class SunDefinitions {
	
	private static final HashMap<Integer, SunDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public int anInt396 = 8;
	public boolean aBool400;
	public int anInt401;
	public int anInt397;
	public int anInt399;
	public int anInt395;
	public int anInt402;
	public int anInt404 = 16777215;
	public int anInt403;
	public int anInt398;
	public int anInt405;
	
	public static final SunDefinitions getDefs(int id) {
		SunDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.SUN.getId(), id);
		defs = new SunDefinitions();
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
			this.anInt396 = stream.readUnsignedShort();
		} else if (opcode == 2) {
			this.aBool400 = true;
		} else if (opcode == 3) {
			this.anInt401 = stream.readShort();
			this.anInt397 = stream.readShort();
			this.anInt399 = stream.readShort();
		} else if (opcode == 4) {
			this.anInt395 = stream.readUnsignedByte();
		} else if (opcode == 5) {
			this.anInt402 = stream.readBigSmart();
		} else if (opcode == 6) {
			this.anInt404 = stream.read24BitUnsignedInteger();
		} else if (opcode == 7) {
			this.anInt403 = stream.readShort();
			this.anInt398 = stream.readShort();
			this.anInt405 = stream.readShort();
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
