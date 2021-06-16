package com.rs.lib.model;

public class ClanMember {
	private String username;
	private ClanRank rank;
	private int job;
	private boolean banFromCitadel;
	private boolean banFromIsland;
	private boolean banFromKeep;
	private long joinDate;
	
	public ClanMember(Account account, ClanRank rank) {
		this.username = account.getUsername();
		this.rank = rank;
		joinDate = System.currentTimeMillis();
	}
	
	public ClanRank getRank() {
		return rank;
	}

	public String getUsername() {
		return username;
	}

	public int getJob() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

	public void setRank(ClanRank rank) {
		this.rank = rank;
	}

	public boolean isBanFromCitadel() {
		return banFromCitadel;
	}

	public void setBanFromCitadel(boolean banFromCitadel) {
		this.banFromCitadel = banFromCitadel;
	}

	public boolean isBanFromKeep() {
		return banFromKeep;
	}

	public void setBanFromKeep(boolean banFromKeep) {
		this.banFromKeep = banFromKeep;
	}

	public boolean isBanFromIsland() {
		return banFromIsland;
	}

	public void setBanFromIsland(boolean banFromIsland) {
		this.banFromIsland = banFromIsland;
	}

	public boolean firstWeek() {
		return System.currentTimeMillis() - joinDate < 7 * 24 * 60 * 60 * 1000;
	}
}
