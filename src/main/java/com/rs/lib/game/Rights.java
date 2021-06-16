package com.rs.lib.game;

public enum Rights {
	PLAYER(0), 
	MOD(1), 
	ADMIN(2), 
	DEVELOPER(2), 
	OWNER(2);
	
	private int crown;
	
	private Rights(int crown) {
		this.crown = crown;
	}
	
	public int getCrown() {
		return crown;
	}
}
