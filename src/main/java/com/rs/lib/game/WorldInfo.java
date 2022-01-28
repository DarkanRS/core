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
package com.rs.lib.game;

public class WorldInfo {
	private int number;
	private String ipAddress;
	private int port;
	private String countryName;
	private String activity;
	private int country;
	private boolean lootShare;
	private boolean members;
	
	public WorldInfo(int number, String ipAddress, int port, String activity, int country, boolean lootShare, boolean members) {
		this.number = number;
		this.ipAddress = ipAddress;
		this.port = port;
		this.activity = activity;
		this.country = country;
		this.countryName = getCountryFromId(country);
		this.lootShare = lootShare;
		this.members = members;
	}
	
	public static String getCountryFromId(int country) {
		return switch(country) {
			case 1 -> "USA";
			default -> "Murica";
		};
	}
	
	public boolean isLobby() {
		return number >= 1000;
	}
	
	public String getActivity() {
		return activity;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public int getCountryId() {
		return country;
	}
	
	public boolean isLootShare() {
		return lootShare;
	}
	
	public boolean isMembers() {
		return members;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	@Override
	public String toString() {
		return "[" + number + ", " + ipAddress + ", " + port + ", " + activity + "]";
	}

	public int getPort() {
		return port;
	}
}
