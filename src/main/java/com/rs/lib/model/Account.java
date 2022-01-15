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
package com.rs.lib.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.rs.lib.game.Rights;
import com.rs.lib.util.Utils;

public class Account {
	
	private String username;
	private String email;
	private String recoveryEmail;
	private String displayName;
	private String prevDisplayName = "";
	private Rights rights;
	private Set<byte[]> prevPasswords;
	private byte[] password;
	private String legacyPass;
	private Social social;
	private long banned;
	private long muted;
	private String lastIp;
	
	public Account(String username) {
		this.username = username;
		this.displayName = Utils.formatPlayerNameForDisplay(username);
		this.prevDisplayName = "";
		this.social = new Social();
	}
	
	public Account(String username, byte[] password, String email) {
		this.username = Utils.formatPlayerNameForProtocol(username);
		this.displayName = Utils.formatPlayerNameForDisplay(username);
		this.prevDisplayName = "";
		this.email = email;
		this.recoveryEmail = email;
		this.rights = Rights.PLAYER;
		this.password = password;
		this.prevPasswords = new HashSet<>();
		this.prevPasswords.add(this.password);
		this.social = new Social();
	}

	public String getUsername() {
		return username;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getEmail() {
		return email;
	}

	public Set<byte[]> getPrevPasswords() {
		return prevPasswords;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.prevPasswords.add(this.password);
		this.password = password;
	}

	public String getLegacyPass() {
		return legacyPass;
	}

	public void setLegacyPass(String legacyPass) {
		this.legacyPass = legacyPass;
	}

	public Social getSocial() {
		return social;
	}
	
	public boolean isMuted() {
		return System.currentTimeMillis() < this.muted;
	}
	
	public void muteSpecific(long ms) {
		this.muted = System.currentTimeMillis() + ms;
	}
	
	public void muteDays(int days) {
		muteSpecific(days * (24 * 60 * 60 * 1000));
	}
	
	public void mutePerm() {
		this.muted = Long.MAX_VALUE;
	}
	
	public void unmute() {
		this.muted = 0;
	}
	
	public String getUnmuteDate() {
		return new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(new Date(muted)); 
	}
	
	public String getUnbanDate() {
		return new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z").format(new Date(banned)); 
	}

	public boolean isBanned() {
		return System.currentTimeMillis() < this.banned;
	}
	
	public void banSpecific(long ms) {
		this.banned = System.currentTimeMillis() + ms;
	}
	
	public void banDays(int days) {
		banSpecific(days * (24 * 60 * 60 * 1000));
	}
	
	public void banPerm() {
		this.banned = Long.MAX_VALUE;
	}
	
	public void unban() {
		this.banned = 0;
	}
	
	public void copyPunishments(Account account) {
		this.banned = account.banned;
		this.muted = account.muted;
	}
	
	public boolean hasRights(Rights rights) {
		return this.rights.ordinal() >= rights.ordinal();
	}

	public Rights getRights() {
		return rights;
	}

	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public String getLastIP() {
		return lastIp;
	}

	public void setLastIP(String lastIp) {
		this.lastIp = lastIp;
	}
	
	public boolean onlineTo(Account other) {
		if (other.getSocial().getStatus() == 2 || (other.getSocial().getStatus() == 1 && !other.getSocial().getFriends().contains(getUsername())))
			return false;
		return true;
	}

	public String getPrevDisplayName() {
		return prevDisplayName;
	}

	public void setPrevDisplayName(String prevDisplayName) {
		this.prevDisplayName = prevDisplayName;
	}

	public String getRecoveryEmail() {
		return recoveryEmail;
	}

	public void setRecoveryEmail(String recoveryEmail) {
		this.recoveryEmail = recoveryEmail;
	}

	public void setSocial(Social social) {
		this.social = social;
	}

	public long getBanExpiry() {
		return banned;
	}

	public long getMuteExpiry() {
		return muted;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
