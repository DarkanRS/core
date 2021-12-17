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

public enum Rights {
	PLAYER(0), 
	MOD(1), 
	ADMIN(2), 
	DEVELOPER(2), 
	OWNER(2);
	
	private int crown;
	
	private Rights(int crown) {
		this.crown = crown;
	}
	
	public int getCrown() {
		return crown;
	}
}
