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
	private Set<DBItemManager> collections = new HashSet<>();
	
	public DBConnection(String mongoUrl) {
		this.mongoUrl = mongoUrl;
	}

	public void init() {
		try {
			Logger logger = (Logger) Logger.getLogger("org.mongodb.driver.cluster");
			logger.setLevel(Level.OFF);
			Logger.getLogger("log").setLevel(Level.OFF);
			client = MongoClients.create(mongoUrl);
			database = client.getDatabase("darkan-server");
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
