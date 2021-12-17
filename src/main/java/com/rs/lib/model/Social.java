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
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.model;

import java.util.HashSet;
import java.util.Set;

public class Social {

	private Set<String> friends;
	private Set<String> ignores;
	private transient Set<String> tillLogoutIgnores;
	private byte status;
	private byte fcStatus;
	private String clanName;
	private boolean connectedToClan;
	private String currentFriendsChat;
	private String guestedClanChat;
	private FriendsChat friendsChat;
	
	public Social() {
		friends = new HashSet<String>(200);
		ignores = new HashSet<String>(100);
		this.friendsChat = new FriendsChat();
		this.currentFriendsChat = "help";
	}
	
	public Set<String> getFriends() {
		return friends;
	}

	public Set<String> getIgnores() {
		return ignores;
	}

	public Set<String> getTillLogoutIgnores() {
		return tillLogoutIgnores;
	}

	public byte getStatus() {
		return status;
	}

	public String getCurrentFriendsChat() {
		return currentFriendsChat;
	}

	public String getGuestedClanChat() {
		return guestedClanChat;
	}

	public FriendsChat getFriendsChat() {
		return friendsChat;
	}
	
	public void setStatus(int status) {
		this.status = (byte) status;
	}

	public void setCurrentFriendsChat(String currentFriendsChat) {
		this.currentFriendsChat = currentFriendsChat;
	}

	public void setGuestedClanChat(String guestedClanChat) {
		this.guestedClanChat = guestedClanChat;
	}

	public void setFriendsChat(FriendsChat friendsChat) {
		this.friendsChat = friendsChat;
	}

	public void addFriend(Account account) {
		friends.add(account.getUsername());
	}

	public void addIgnore(Account account) {
		ignores.add(account.getUsername());
	}

	public void removeFriend(Account account) {
		friends.remove(account.getUsername());
	}

	public void removeIgnore(Account account) {
		ignores.remove(account.getUsername());
	}

	public String getClanName() {
		return clanName;
	}

	public void setClanName(String clan) {
		this.clanName = clan;
	}

	public boolean isConnectedToClan() {
		return connectedToClan;
	}

	public void setConnectedToClan(boolean connectedToClan) {
		this.connectedToClan = connectedToClan;
	}

	public byte getFcStatus() {
		return fcStatus;
	}

	public void setFcStatus(int fcStatus) {
		this.fcStatus = (byte) fcStatus;
	}
	
}
