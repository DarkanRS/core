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

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cache.loaders.ObjectType;

public class WorldObject {
	
	protected WorldTile tile;
	protected int id;
	protected ObjectType type;
	protected int rotation;

	public WorldObject(int id, ObjectType type, int rotation, WorldTile tile) {
		this.tile = tile;
		this.id = id;
		this.type = type;
		this.rotation = rotation;
	}
	
	public WorldObject(int id, int rotation, int x, int y, int plane) {
		this.tile = WorldTile.of(x, y, plane);
		this.id = id;
		this.type = ObjectDefinitions.getDefs(id).getType(0);
		this.rotation = rotation;
	}

	public WorldObject(int id, ObjectType type, int rotation, int x, int y, int plane) {
		this.tile = WorldTile.of(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
	}

	public WorldObject(WorldObject object) {
		this.tile = object.tile;
		this.id = object.id;
		this.type = object.type;
		this.rotation = object.rotation;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCoordFaceX() {
		return tile.getCoordFaceX(getDefinitions().sizeX, getDefinitions().sizeY, rotation);
	}
	
	public int getCoordFaceY() {
		return tile.getCoordFaceY(getDefinitions().sizeX, getDefinitions().sizeY, rotation);
	}
	
	public WorldTile getCoordFace() {
		return WorldTile.of(getCoordFaceX(), getCoordFaceY(), tile.getPlane());
	}
	
	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getDefs(id);
	}
	
	public int getRotation(int turn) {
		int initial = rotation;
		if (turn == 0)
			return initial;
		if (turn > 0) {
			for (int i = 0;i < turn;i++) {
				initial++;
				if (initial == 4)
					initial = 0;
			}
		} else {
			for (int i = 0;i > turn;i--) {
				initial--;
				if (initial == -1)
					initial = 3;
			}
		}
		return initial;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public WorldTile getTile() {
		return tile;
	}
	
	public void setTile(WorldTile tile) {
		this.tile = tile;
	}
	
	public ObjectType getType() {
		return type;
	}
	
	public void setType(ObjectType i) {
		this.type = i;
	}

	public int getRotation() {
		return rotation;
	}
	
	public int getSlot() {
		return type.slot;
	}
	
	public int getX() {
		return tile.getX();
	}
	
	public int getY() {
		return tile.getY();
	}
	
	public int getPlane() {
		return tile.getPlane();
	}
}
