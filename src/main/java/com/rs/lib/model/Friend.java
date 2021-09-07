package com.rs.lib.model;

import com.rs.lib.game.WorldInfo;

public class Friend {

	private Account account;
	private WorldInfo world;
	private boolean offline;
	
	public Friend(Account account, WorldInfo world, boolean offline) {
		this.account = account;
		this.world = world;
		this.offline = offline;
	}
	
	public Account getAccount() {
		return account;
	}

	public WorldInfo getWorld() {
		return world;
	}

	public boolean isOffline() {
		return offline;
	}

	public boolean onlineTo(Account player) {
		return account.onlineTo(player);
	}
}
