package com.rs.lib.util;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoUtil {
	
	public static boolean collectionExists(MongoDatabase db, String collectionName) {
	    MongoIterable<String> collectionNames = db.listCollectionNames();
	    for (String name : collectionNames) {
	        if (name.equalsIgnoreCase(collectionName)) {
	            return true;
	        }
	    }
	    return false;
	}

}
