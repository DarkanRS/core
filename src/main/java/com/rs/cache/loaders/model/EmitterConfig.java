package com.rs.cache.loaders.model;

public class EmitterConfig {
	public int type;
	public int face;
	public int faceX;
	public int faceY;
	public int faceZ;
	public byte priority;
	
	EmitterConfig(int type, int face, int faceX, int faceY, int faceZ, byte priority) {
		this.type = type;
		this.face = face;
		this.faceX = faceX;
		this.faceY = faceY;
		this.faceZ = faceZ;
		this.priority = priority;
	}
}
