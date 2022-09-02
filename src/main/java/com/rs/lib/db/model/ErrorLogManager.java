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
package com.rs.lib.db.model;

import static com.mongodb.client.model.Filters.eq;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rs.lib.db.DBItemManager;
import com.rs.lib.file.JsonFileManager;

public class ErrorLogManager extends DBItemManager {

	public ErrorLogManager() {
		super("logs");
	}

	@Override
	public void initCollection() {
		getDocs().createIndex(Indexes.text("type"));
		getDocs().createIndex(Indexes.descending("hash"));
		getDocs().createIndex(Indexes.ascending("date"), new IndexOptions().expireAfter(180L, TimeUnit.DAYS));
	}

	public void save(LogEntry entry) {
		save(entry, null);
	}

	public void save(LogEntry entry, Runnable done) {
		execute(() -> {
			saveSync(entry);
			if (done != null)
				done.run();
		});
	}

	public void saveSync(LogEntry entry) {
		try {
			getDocs().findOneAndReplace(eq("hash", entry.getHash()), Document.parse(JsonFileManager.toJson(entry)), new FindOneAndReplaceOptions().upsert(true));
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public void logError(String logger, Throwable throwable) {
		String stackTrace = logger + " - " + throwable.getMessage() + "\r\n";
		CharArrayWriter cw = new CharArrayWriter();
		PrintWriter w = new PrintWriter(cw);
		throwable.printStackTrace(w);
		w.close();
		stackTrace += cw.toString();
		save(new LogEntry(LogEntry.LogType.ERROR, stackTrace.hashCode(), stackTrace));
	}
	
	public void logError(String logger, String message, Throwable throwable) {
		String stackTrace = logger + " - " + throwable.getMessage() + " - " + message + "\r\n";
		CharArrayWriter cw = new CharArrayWriter();
		PrintWriter w = new PrintWriter(cw);
		throwable.printStackTrace(w);
		w.close();
		stackTrace += cw.toString();
		save(new LogEntry(LogEntry.LogType.ERROR, stackTrace.hashCode(), stackTrace));
	}

	public void logError(String logger, String error) {
		save(new LogEntry(LogEntry.LogType.ERROR, error.hashCode(), logger + " - " + error));
	}
}
