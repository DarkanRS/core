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
		this.executor = Executors.newSingleThreadExecutor(new DBThreadFactory());
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
