// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
