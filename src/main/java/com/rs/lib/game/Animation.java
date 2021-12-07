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

import com.rs.cache.loaders.animations.AnimationDefinitions;

public final class Animation {

	private int[] ids;
	private int speed;

	public Animation(int id) {
		this(id, 0);
	}

	public Animation(int id, int speed) {
		this(id, id, id, id, speed);
	}

	public Animation(int id1, int id2, int id3, int id4, int speed) {
		this.ids = new int[] { id1, id2, id3, id4 };
		this.speed = speed;
	}

	public int[] getIds() {
		return ids;
	}

	public int getSpeed() {
		return speed;
	}
	
	public AnimationDefinitions getDefs() {
		return AnimationDefinitions.getDefs(ids[0]);
	}
}
