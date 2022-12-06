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
package com.rs.lib.util;

import com.rs.lib.game.WorldTile;

public class Vec2 {
	
	private float x, y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(WorldTile tile) {
		this.x = tile.getX();
		this.y = tile.getY();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Vec2 sub(Vec2 v2) {
		return new Vec2(this.x - v2.x, this.y - v2.y);
	}
	
	public void norm() {
		float mag = (float) Math.sqrt(this.x*this.x + this.y*this.y);
		this.x = (float) (this.x / mag);
		this.y = (float) (this.y / mag);
	}
	
	public WorldTile toTile() {
		return toTile(0);
	}

	public WorldTile toTile(int plane) {
		return WorldTile.of((int) Math.round(this.x), (int) Math.round(this.y), plane);
	}
	
	@Override
	public String toString() {
		return "[" + x +","+y+"]";
	}
}
