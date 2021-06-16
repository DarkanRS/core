package com.rs.lib.model;

import java.util.HashMap;
import java.util.Map;

import com.rs.lib.util.Utils;

public class FriendsChat {
	
	private String name;
	private Map<String, Integer> friendsChatRanks;
	private byte rankToEnter;
	private byte rankToSpeak;
	private byte rankToKick;
	private byte rankToLS;
	private int lsp;
	private boolean coinshare;
	private byte fcStatus;
	
	public FriendsChat() {
		friendsChatRanks = new HashMap<>(200);
		rankToKick = 7;
		rankToLS = -1;
	}
	
	public int getRank(String username) {
		Integer rank = getFriendsChatRanks().get(username);
		if (rank == null)
			return -1;
		return rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Integer> getFriendsChatRanks() {
		return friendsChatRanks;
	}

	public void setFriendsChatRanks(Map<String, Integer> friendsChatRanks) {
		this.friendsChatRanks = friendsChatRanks;
	}

	public byte getRankToEnter() {
		return rankToEnter;
	}

	public void setRankToEnter(byte rankToEnter) {
		this.rankToEnter = rankToEnter;
	}

	public byte getRankToSpeak() {
		return rankToSpeak;
	}

	public void setRankToSpeak(byte rankToSpeak) {
		this.rankToSpeak = rankToSpeak;
	}

	public byte getRankToKick() {
		return rankToKick;
	}

	public void setRankToKick(byte rankToKick) {
		this.rankToKick = rankToKick;
	}

	public byte getRankToLS() {
		return rankToLS;
	}

	public void setRankToLS(byte rankToLS) {
		this.rankToLS = rankToLS;
	}

	public int getLsp() {
		return lsp;
	}

	public void setLsp(int lsp) {
		this.lsp = lsp;
	}

	public boolean isCoinshare() {
		return coinshare;
	}

	public void setCoinshare(boolean coinshare) {
		this.coinshare = coinshare;
	}

	public byte getFcStatus() {
		return fcStatus;
	}

	public void setFcStatus(byte fcStatus) {
		this.fcStatus = fcStatus;
	}

	public void setRank(String name, int rank) {
		if (rank < 0 || rank > 6)
			return;
		getFriendsChatRanks().put(Utils.formatPlayerNameForProtocol(name), rank);
	}
}
