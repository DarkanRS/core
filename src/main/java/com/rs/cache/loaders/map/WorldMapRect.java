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
package com.rs.cache.loaders.map;

public class WorldMapRect {

	public int plane;
	public int bottomLeftX;
	public int bottomLeftY;
	public int topRightX;
	public int topRightY;
	public int bestBottomLeftX;
	public int bestBottomLeftY;
	public int bestTopRightX;
	public int bestTopRightY;

	WorldMapRect(int plane, int bottomLeftX, int bottomLeftY, int topRightX, int topRightY, int bestBottomLeftX, int bestBottomLeftY, int bestTopRightX, int bestTopRightY) {
		this.plane = plane;
		this.bottomLeftX = bottomLeftX;
		this.bottomLeftY = bottomLeftY;
		this.topRightX = topRightX;
		this.topRightY = topRightY;
		this.bestBottomLeftX = bestBottomLeftX;
		this.bestBottomLeftY = bestBottomLeftY;
		this.bestTopRightX = bestTopRightX;
		this.bestTopRightY = bestTopRightY;
	}

	boolean method12408(int plane, int x, int y) {
		return this.plane == plane && x >= bottomLeftX && x <= topRightX && y >= bottomLeftY && y <= topRightY;
	}

	boolean method12409(int x, int y) {
		return x >= bestBottomLeftX && x <= bestTopRightX && y >= bestBottomLeftY && y <= bestTopRightY;
	}

	void method12410(int i_1, int i_2, int[] ints_3) {
		ints_3[0] = plane;
		ints_3[1] = bottomLeftX - bestBottomLeftX + i_1;
		ints_3[2] = bottomLeftY - bestBottomLeftY + i_2;
	}

	void method12414(int i_1, int i_2, int[] ints_3) {
		ints_3[0] = 0;
		ints_3[1] = bestBottomLeftX - bottomLeftX + i_1;
		ints_3[2] = bestBottomLeftY - bottomLeftY + i_2;
	}

	boolean method12415(int x, int y) {
		return x >= bottomLeftX && x <= topRightX && y >= bottomLeftY && y <= topRightY;
	}

}
