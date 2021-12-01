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
package com.rs.lib.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonFileManager {
	
	private static Gson GSON;
	
	public static void setGSON(Gson gson) {
		GSON = gson;
	}
	
	public static Gson getGson() {
		return GSON;
	}

	public static <T> T loadJsonFile(File f, Type clazz) throws JsonIOException, IOException {
		if (!f.exists())
			return null;
		JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        T obj = GSON.fromJson(reader, clazz);
        reader.close();
		return obj;
	}
	
	public static <T> T fromJSONString(String json, Type clazz) throws JsonIOException, IOException {
        T obj = GSON.fromJson(json, clazz);
		return obj;
	}

	public static final void saveJsonFile(Object o, File f) throws JsonIOException, IOException {
		File dir = new File(f.getParent());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
        writer.setIndent("  ");
        GSON.toJson(o, o.getClass(), writer);
        writer.flush();
        writer.close();
	}

	public static String toJson(Object o) {
		return GSON.toJson(o);
	}

}