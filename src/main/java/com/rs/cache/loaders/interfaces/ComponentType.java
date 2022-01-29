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
public enum ComponentType {
	CONTAINER(0),
	TYPE_1(1),
	TYPE_2(2),
	FIGURE(3), 
	TEXT(4), 
	SPRITE(5), 
	MODEL(6),
	TYPE_7(7),
	TYPE_8(8),
	LINE(9);

	public static ComponentType forId(int id) {
		for (ComponentType t : ComponentType.values()) {
			if (t.id == id)
				return t;
		}
		return null;
	}

	private int id;

	private ComponentType(int id) {
		this.id = id;
	}
}