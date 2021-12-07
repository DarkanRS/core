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

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class QCMesDefinitions {
	
	private static final HashMap<Integer, QCMesDefinitions> CACHE = new HashMap<>();
	
	public int id;
	public String[] message;
	public int[] responses;
	public QCValueType[] types;
	public int[][] configs;
	public boolean searchable = true;
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.QC_MESSAGES).getLastFileId(1)+1;i++) {
			QCMesDefinitions defs = getDefs(i);
			if (defs == null)
				continue;
//			for (String str : defs.message) {
//				if (str.contains("My current Slayer assignment is"))
//					System.out.println(defs);
//			}
			if (defs.types == null)
				continue;
			for (QCValueType type : defs.types) {
				if (type == QCValueType.TOSTRING_SHARED)
					System.out.println(defs);
			}
		}
		
//		QCMesDefinitions defs = getDefs(1108);
//		System.out.println(defs);
	}
	
	public static final QCMesDefinitions getDefs(int id) {
		QCMesDefinitions defs = CACHE.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.QC_MESSAGES).getFile(1, id);
		defs = new QCMesDefinitions();
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
			this.message = Utils.splitByChar(buffer.readString(), '<');
		} else if (opcode == 2) {
			int i_4 = buffer.readUnsignedByte();
			this.responses = new int[i_4];

			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.responses[i_5] = buffer.readUnsignedShort();
			}
		} else if (opcode == 3) {
			int count = buffer.readUnsignedByte();
			this.types = new QCValueType[count];
			this.configs = new int[count][];

			for (int i = 0; i < count; i++) {
				int typeId = buffer.readUnsignedShort();
				QCValueType type = QCValueType.get(typeId);
				if (type != null) {
					this.types[i] = type;
					this.configs[i] = new int[type.paramCount];

					for (int config = 0; config < type.paramCount; config++) {
						this.configs[i][config] = buffer.readUnsignedShort();
					}
				}
			}
		} else if (opcode == 4) {
			this.searchable = false;
		}
	}
	
	public enum QCValueType {
		LISTDIALOG(0, 2, 2, 1),
		OBJDIALOG(1, 2, 2, 0),
		COUNTDIALOG(2, 4, 4, 0),
		STAT_BASE(4, 1, 1, 1),
		ENUM_STRING(6, 0, 4, 2),
		ENUM_STRING_CLAN(7, 0, 1, 1),
		TOSTRING_VARP(8, 0, 4, 1),
		TOSTRING_VARBIT(9, 0, 4, 1),
		OBJTRADEDIALOG(10, 2, 2, 0),
		ENUM_STRING_STATBASE(11, 0, 1, 2),
		ACC_GETCOUNT_WORLD(12, 0, 1, 0),
		ACC_GETMEANCOMBATLEVEL(13, 0, 1, 0),
		TOSTRING_SHARED(14, 0, 4, 1),
		ACTIVECOMBATLEVEL(15, 0, 1, 0);
		
		private static HashMap<Integer, QCValueType> MAP = new HashMap<>();
		
		static {
			for (QCValueType type : QCValueType.values()) {
				MAP.put(type.id, type);
			}
		}
		
		public int id, clientSize, serverSize, paramCount;
		private QCValueType(int id, int clientSize, int serverSize, int paramCount) {
			this.id = id;
			this.clientSize = clientSize;
			this.serverSize = serverSize;
			this.paramCount = paramCount;
		}
		
		public static QCValueType get(int id) {
			return MAP.get(id);
		}
	}
	
	public String getFilledQuickchat(InputStream stream) {
		StringBuilder builder = new StringBuilder(80);
		if (this.types != null) {
			for (int i = 0; i < this.types.length; i++) {
				builder.append(this.message[i]);
				builder.append(getFilledValue(this.types[i], this.configs[i], stream.readSized(this.types[i].serverSize)));
			}
		}

		builder.append(this.message[this.message.length - 1]);
		return builder.toString();
	}

	public String getFilledValue(QCValueType type, int[] configs, long data) {
		if (type == QCValueType.LISTDIALOG) {
			EnumDefinitions enumDef = EnumDefinitions.getEnum(configs[0]);
			return enumDef.getStringValue((int) data);
		} else if (type != QCValueType.OBJDIALOG && type != QCValueType.OBJTRADEDIALOG) {
			return type != QCValueType.ENUM_STRING && type != QCValueType.ENUM_STRING_CLAN && type != QCValueType.ENUM_STRING_STATBASE ? null : EnumDefinitions.getEnum(configs[0]).getStringValue((int) data);
		} else {
			return ItemDefinitions.getDefs((int) data).name;
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
