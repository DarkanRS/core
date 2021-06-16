package com.rs.cache.loaders.model;

public class BillBoardConfig {
	
	public int type;
	public int face;
	public int priority;
	public int magnitude;
	
	BillBoardConfig(int type, int face, int priority, int magnitude) {
		this.type = type;
		this.face = face;
		this.priority = priority;
		this.magnitude = magnitude;
	}
	
	BillBoardConfig method1459(int face) {
		return new BillBoardConfig(this.type, face, this.priority, this.magnitude);
	}
}
