package com.rs.lib.db;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.rs.lib.util.MongoUtil;

public abstract class DBItemManager {

	private String collection;
	private DBConnection conn;
	private MongoCollection<Document> documents;
	
	public DBItemManager(String collection) {
		this.collection = collection;
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
	
}
