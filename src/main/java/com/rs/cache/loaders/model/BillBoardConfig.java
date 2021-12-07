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
package com.rs.cache.loaders.model;

public class BillBoardConfig {
	
	public int type;
	public int face;
	public int priority;
	public int magnitude;
	
	BillBoardConfig(int type, int face, int priority, int magnitude) {
		this.type = type;
		this.face = face;
		this.priority = priority;
		this.magnitude = magnitude;
	}
	
	BillBoardConfig method1459(int face) {
		return new BillBoardConfig(this.type, face, this.priority, this.magnitude);
	}
}
