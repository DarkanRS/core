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
package com.rs.cache.loaders.cs2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public final class CS2ParamDefs {

	public int id;
	public int defaultInt;
	public boolean autoDisable = true;
	public char charVal;
	public String defaultString;
	public CS2Type type;

	private static final ConcurrentHashMap<Integer, CS2ParamDefs> maps = new ConcurrentHashMap<Integer, CS2ParamDefs>();
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		File file = new File("params.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append("//Version = 727\n");
		writer.flush();
		for (int i = 0; i < Cache.STORE.getIndex(IndexType.CONFIG).getValidFilesCount(ArchiveType.PARAMS.getId()); i++) {
			CS2ParamDefs param = getParams(i);
			if (param == null)
				continue;
			writer.append(i + " - '"+param.charVal+"'->" + param.type + ", "+param.autoDisable+" int: " + param.defaultInt + " str:\"" + param.defaultString + "\"");
			writer.newLine();
			writer.flush();
		}
		writer.close();
	}

	public static final CS2ParamDefs getParams(int paramId) {
		CS2ParamDefs param = maps.get(paramId);
		if (param != null)
			return param;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.PARAMS.getId(), paramId);
		param = new CS2ParamDefs();
		param.id = paramId;
		if (data != null)
			param.readValueLoop(new InputStream(data));
		maps.put(paramId, param);
		return param;
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
			charVal = Utils.cp1252ToChar((byte) stream.readByte());
			type = CS2Type.forJagexDesc(charVal);
		} else if (opcode == 2) {
			defaultInt = stream.readInt();
		} else if (opcode == 4) {
			autoDisable = false;
		} else if (opcode == 5) {
			defaultString = stream.readString();
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
