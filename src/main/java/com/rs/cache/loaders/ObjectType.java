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
package com.rs.cache.loaders;

import java.util.HashMap;
import java.util.Map;

public enum ObjectType {
    WALL_STRAIGHT(0, 0),
    WALL_DIAGONAL_CORNER(1, 0),
    WALL_WHOLE_CORNER(2, 0),
    WALL_STRAIGHT_CORNER(3, 0),
    STRAIGHT_INSIDE_WALL_DEC(4, 1),
    STRAIGHT_OUSIDE_WALL_DEC(5, 1),
    DIAGONAL_OUTSIDE_WALL_DEC(6, 1),
    DIAGONAL_INSIDE_WALL_DEC(7, 1),
    DIAGONAL_INWALL_DEC(8, 1),
    WALL_INTERACT(9, 2),
    SCENERY_INTERACT(10, 2),
    GROUND_INTERACT(11, 2),
    STRAIGHT_SLOPE_ROOF(12, 2),
    DIAGONAL_SLOPE_ROOF(13, 2),
    DIAGONAL_SLOPE_CONNECT_ROOF(14, 2),
    STRAIGHT_SLOPE_CORNER_CONNECT_ROOF(15, 2),
    STRAIGHT_SLOPE_CORNER_ROOF(16, 2),
    STRAIGHT_FLAT_ROOF(17, 2),
    STRAIGHT_BOTTOM_EDGE_ROOF(18, 2),
    DIAGONAL_BOTTOM_EDGE_CONNECT_ROOF(19, 2),
    STRAIGHT_BOTTOM_EDGE_CONNECT_ROOF(20, 2),
    STRAIGHT_BOTTOM_EDGE_CONNECT_CORNER_ROOF(21, 2),
    GROUND_DECORATION(22, 3);
	
	private static Map<Integer, ObjectType> MAP = new HashMap<>();
	
	static {
		for (ObjectType t : ObjectType.values())
			MAP.put(t.id, t);
	}
	
	public static ObjectType forId(int type) {
		return MAP.get(type);
	}

    public final int id;
    public final int slot;

    ObjectType(int type, int slot) {
        this.id = type;
        this.slot = slot;
    }
    
    public boolean isWall() {
        return id >= ObjectType.WALL_STRAIGHT.id && id <= ObjectType.WALL_STRAIGHT_CORNER.id || id == ObjectType.WALL_INTERACT.id;
    }
}