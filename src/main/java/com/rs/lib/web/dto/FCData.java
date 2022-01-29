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
package com.rs.lib.web.dto;

import java.util.Set;

import com.rs.lib.game.Rights;
import com.rs.lib.model.Account;
import com.rs.lib.model.FriendsChat;
import com.rs.lib.model.FriendsChat.Rank;

public class FCData {

	private String ownerUsername;
	private String ownerDisplayName;
	private FriendsChat settings;
	private Set<String> usernames;
	
	public FCData(String ownerUsername, String ownerDisplayName, FriendsChat settings, Set<String> usernames) {
		this.ownerUsername = ownerUsername;
		this.ownerDisplayName = ownerDisplayName;
		this.settings = settings;
		this.usernames = usernames;
	}
	
	public Rank getRank(Account account) {
		return getRank(account.getRights(), account.getUsername());
	}

	private Rank getRank(Rights rights, String username) {
		if (rights.ordinal() >= Rights.ADMIN.ordinal())
			return Rank.JMOD;
		if (username.equals(ownerUsername))
			return Rank.OWNER;
		return settings.getRank(username);
	}
	
	public Set<String> getUsernames() {
		return usernames;
	}

	public FriendsChat getSettings() {
		return settings;
	}
	
	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}
}
