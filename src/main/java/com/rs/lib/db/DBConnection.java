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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
	
	private MongoClient client;
	private MongoDatabase database;
	private String mongoUrl;
	private String dbName;
	private Set<DBItemManager> collections = new HashSet<>();
	
	public DBConnection(String mongoUrl, String dbName) {
		this.mongoUrl = mongoUrl;
		this.dbName = dbName;
	}

	public void init() {
		try {
			Logger logger = (Logger) Logger.getLogger("org.mongodb.driver.cluster");
			logger.setLevel(Level.OFF);
			Logger.getLogger("log").setLevel(Level.OFF);
			client = MongoClients.create(mongoUrl);
			database = client.getDatabase(dbName);
			for (DBItemManager coll : collections)
				coll.init(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error connecting to mongo database.");

		}
	}
	
	public void addItemManager(DBItemManager mgr) {
		collections.add(mgr);
	}
	
	public MongoDatabase getDb() {
		return database;
	}

}
