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

public enum ClanRank {
	NONE(-1),
	RECRUIT(0),
	CORPORAL(1),
	SERGEANT(2),
	LIEUTENANT(3),
	CAPTAIN(4),
	ADMIN(100),
	GENERAL(5),
	ORGANIZER(101),
	COORDINATOR(102),
	OVERSEER(103),
	DEPUTY_OWNER(125),
	OWNER(126);
	
	public static ClanRank forId(int rank) {
		for (ClanRank r : ClanRank.values())
			if (r.iconId == rank)
				return r;
		return RECRUIT;
	}
	
	private final int iconId;
	
	private ClanRank(int iconId) {
		this.iconId = iconId;
	}

	public int getIconId() {
		return iconId;
	}
}
