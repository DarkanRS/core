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

	public void addFriend(String name) {
		friends.add(name);
	}

	public void addIgnore(String name) {
		ignores.add(name);
	}

	public void removeFriend(String name) {
		friends.remove(name);
	}

	public void removeIgnore(String name) {
		ignores.remove(name);
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

	public boolean onlineTo(Account other) {
		if (other.getSocial().getStatus() == 2 || (other.getSocial().getStatus() == 1 && !friends.contains(other.getUsername())))
			return false;
		return true;
	}
	
}
