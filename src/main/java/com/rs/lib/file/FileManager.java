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
