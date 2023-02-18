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
package com.rs.lib.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.rs.lib.thread.CatchExceptionRunnable;
import com.rs.lib.util.MongoUtil;

public abstract class DBItemManager {

	private ExecutorService executor;
	private String collection;
	private DBConnection conn;
	private MongoCollection<Document> documents;
	
	public DBItemManager(String collection) {
		this.collection = collection;
		this.executor = Executors.newVirtualThreadPerTaskExecutor();
	}

	public void init(DBConnection conn) {
		this.conn = conn;
		if (!MongoUtil.collectionExists(conn.getDb(), collection)) {
			conn.getDb().createCollection(collection);
			documents = conn.getDb().getCollection(collection);
			initCollection();
		} else
			documents = conn.getDb().getCollection(collection);
	}
	
	public abstract void initCollection();

	public DBConnection getConn() {
		return conn;
	}
	
	public MongoCollection<Document> getDocs() {
		return documents;
	}
	
	public void execute(Runnable task) {
		executor.execute(new CatchExceptionRunnable(task));
	}
}
