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
package com.rs.lib.file;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.rs.lib.util.Utils;

public class FileManager {

	public static void writeToFile(String fileName, String text) {
		try {
			String[] parts = fileName.split("/");

			File file;
			for (int i = 0; i < parts.length - 1; ++i) {
				file = new File("./logs/" + parts[i]);
				if (!file.exists()) {
					file.mkdir();
				}
			}
			FileWriter writer = new FileWriter("./logs/" + fileName, true);
			writer.write("[" + Utils.getDateString() + "]:  "+ text + "\r\n");
			writer.close();
		} catch (Exception e) {

		}
	}

	public static void logError(Throwable throwable) {
		StringWriter errors = new StringWriter();
		throwable.printStackTrace(new PrintWriter(errors));
		writeToFile("errorLog.txt", Thread.currentThread().getName() + ": " + errors.toString());
	}

	public static void logError(String string) {
		writeToFile("errorLog.txt", string);
	}

}
