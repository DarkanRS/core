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
package com.rs.cache.loaders.interfaces;

public enum ModelType {
	NONE(0),
	RAW_MODEL(1),
	NPC_HEAD(2),
	PLAYER_HEAD(3),
	ITEM(4),
	PLAYER_MODEL(5),
	NPC_MODEL(6),
	PLAYER_HEAD_IGNOREWORN(7),
	ITEM_CONTAINER_MALE(8),
	ITEM_CONTAINER_FEMALE(9);
	
	public static ModelType forId(int id) {
		for (ModelType t : ModelType.values()) {
			if (t.id == id)
				return t;
		}
		return null;
	}

	private int id;

	private ModelType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
