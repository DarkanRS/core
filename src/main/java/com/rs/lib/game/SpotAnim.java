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
package com.rs.lib.game;

public final class SpotAnim {

	private int id, height, speed, rotation;

	public SpotAnim(int id) {
		this(id, 0, 0, 0);
	}

	public SpotAnim(int id, int speed) {
		this(id, speed, 0);
	}

	public SpotAnim(int id, int speed, int height) {
		this(id, speed, height, 0);
	}

	public SpotAnim(int id, int speed, int height, int rotation) {
		this.id = id;
		this.speed = speed;
		this.height = height;
		this.rotation = rotation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + id;
		result = prime * result + rotation;
		result = prime * result + speed;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpotAnim other = (SpotAnim) obj;
		if (height != other.height)
			return false;
		if (id != other.id)
			return false;
		if (rotation != other.rotation)
			return false;
		if (speed != other.speed)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public int getSettingsHash() {
		return (speed & 0xffff) | (height << 16);
	}

	public int getSettings2Hash() {
		int hash = 0;
		hash |= rotation & 0x7;
		// hash |= value << 3;
		// hash |= 1 << 7; boolean
		return hash;
	}

	public int getSpeed() {
		return speed;
	}

	public int getHeight() {
		return height;
	}
}
