package com.rs.lib.web.dto;

import com.rs.lib.game.WorldInfo;
import com.rs.lib.model.Account;

public class WorldPlayerAction {
	
	private Account account;
	private WorldInfo world;
	
	public WorldPlayerAction(Account account, WorldInfo world) {
		this.account = account;
		this.world = world;
	}

	public WorldInfo getWorld() {
		return world;
	}

	public Account getAccount() {
		return account;
	}
	
}
