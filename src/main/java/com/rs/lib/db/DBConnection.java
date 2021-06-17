package com.rs.lib.db;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.rs.lib.thread.CatchExceptionRunnable;

public class DBConnection {
	
	private ExecutorService executor;
	private MongoClient client;
	private MongoDatabase database;
	private String mongoUrl;
	private Set<DBItemManager> collections = new HashSet<>();
	
	public DBConnection(String mongoUrl) {
		this.executor = Executors.newSingleThreadExecutor(new DBThreadFactory());
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

	public void execute(Runnable task) {
		executor.execute(new CatchExceptionRunnable(task));
	}

}
