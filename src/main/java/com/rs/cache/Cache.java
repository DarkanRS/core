package com.rs.cache;

import java.io.IOException;

public final class Cache {

	public static Store STORE;

	private Cache() {

	}
	
	public static void init(String path) throws IOException {
		STORE = new Store(path, false);
	}
}
