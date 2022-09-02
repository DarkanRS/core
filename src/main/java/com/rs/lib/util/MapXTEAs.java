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
package com.rs.lib.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.rs.lib.file.JsonFileManager;

public final class MapXTEAs {
	
	private static Map<Integer, int[]> KEYS = new HashMap<>();
	private final static String PATH = "./data/map/xteaKeys.json";

	public static final int[] getMapKeys(int regionId) {
		int[] arr = (int[]) KEYS.get(regionId);
		if (arr == null || (arr[0] == 0 && arr[1] == 0 && arr[2] == 0 && arr[3] == 0))
			return null;
		return arr;
	}

	public static void loadKeys() throws JsonIOException, IOException {
		Logger.info(new Object() {}, "Loading XTEAs...");
		if (!new File(PATH).exists())
			throw new FileNotFoundException("No map keys file found!");
		else
			KEYS = JsonFileManager.loadJsonFile(new File(PATH), new TypeToken<Map<Integer, int[]>>(){}.getType());
	}
}
