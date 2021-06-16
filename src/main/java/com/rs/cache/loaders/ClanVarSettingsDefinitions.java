package com.rs.cache.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class ClanVarSettingsDefinitions {
	
	private static final HashMap<Integer, ClanVarSettingsDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public char aChar4832;
	public int baseVar;
	public int startBit;
	public int endBit;
	
	public static final ClanVarSettingsDefinitions getDefs(int id) {
		ClanVarSettingsDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.CLAN_VAR_SETTINGS.getId(), id);
		
		defs = new ClanVarSettingsDefinitions();
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
		} else if (opcode == 2) {
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
