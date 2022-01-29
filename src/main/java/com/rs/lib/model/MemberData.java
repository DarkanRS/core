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

public class MemberData {
	private ClanRank rank;
	private int job;
	private boolean banFromCitadel;
	private boolean banFromIsland;
	private boolean banFromKeep;
	private long joinDate;
	
	public MemberData(ClanRank rank) {
		this.rank = rank;
		joinDate = System.currentTimeMillis();
	}
	
	public ClanRank getRank() {
		return rank;
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
