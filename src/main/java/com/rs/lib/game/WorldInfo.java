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

public record WorldInfo(int number, String ipAddress, int port, String activity, int country, boolean lootShare, boolean members) {
	
	public static String getCountryFromId(int country) {
		return switch(country) {
			case 1 -> "USA";
			default -> "Murica";
		};
	}
	
	public String getCountryName() {
		return getCountryFromId(country);
	}
	
	public boolean isLobby() {
		return number >= 1000;
	}
	
	@Override
	public String toString() {
		return "[" + number + ", " + ipAddress + ", " + port + ", " + activity + "]";
	}
}
