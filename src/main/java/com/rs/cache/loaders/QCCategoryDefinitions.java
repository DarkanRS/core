package com.rs.cache.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class QCCategoryDefinitions {
	
	private static final HashMap<Integer, QCCategoryDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public String name;
	public int[] subCategories;
	public char[] subCategoryHotkeys;
	public int[] messages;
	public char[] messageHotkeys;
	
	public static final QCCategoryDefinitions getDefs(int id) {
		QCCategoryDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.QC_MENUS).getFile(0, id);
		defs = new QCCategoryDefinitions();
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
		method15213();
	}
	
	void method15213() {
		int i_2;
		if (this.messages != null) {
			for (i_2 = 0; i_2 < this.messages.length; i_2++) {
				this.messages[i_2] |= 0x8000;
			}
		}

		if (this.subCategories != null) {
			for (i_2 = 0; i_2 < this.subCategories.length; i_2++) {
				this.subCategories[i_2] |= 0x8000;
			}
		}
	}

	private void readValues(InputStream buffer, int opcode) {
		if (opcode == 1) {
			this.name = buffer.readString();
		} else if (opcode == 2) {
			int count = buffer.readUnsignedByte();
			this.subCategories = new int[count];
			this.subCategoryHotkeys = new char[count];

			for (int i_5 = 0; i_5 < count; i_5++) {
				this.subCategories[i_5] = buffer.readUnsignedShort();
				byte b_6 = (byte) buffer.readByte();
				this.subCategoryHotkeys[i_5] = b_6 == 0 ? 0 : Utils.cp1252ToChar(b_6);
			}
		} else if (opcode == 3) {
			int i_4 = buffer.readUnsignedByte();
			this.messages = new int[i_4];
			this.messageHotkeys = new char[i_4];

			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.messages[i_5] = buffer.readUnsignedShort();
				byte b_6 = (byte) buffer.readByte();
				this.messageHotkeys[i_5] = b_6 == 0 ? 0 : Utils.cp1252ToChar(b_6);
			}
		} else if (opcode == 4) {
			return;
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
