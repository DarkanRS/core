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
package com.rs.lib.web.dto;

import com.rs.lib.game.Rights;
import com.rs.lib.model.Account;
import com.rs.lib.model.Social;

public class ExposedAccount {
	
	private String username;
	private String displayName;
	private Rights rights;
	private Social social;
	
	public ExposedAccount(Account account) {
		this.username = account.getUsername();
		this.displayName = account.getDisplayName();
		this.rights = account.getRights();
		this.social = account.getSocial();
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getUsername() {
		return username;
	}

	public Rights getRights() {
		return rights;
	}

	public Social getSocial() {
		return social;
	}

}
