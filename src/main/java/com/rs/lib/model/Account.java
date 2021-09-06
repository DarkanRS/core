package com.rs.lib.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.rs.lib.game.Rights;
import com.rs.lib.util.Crypt;
import com.rs.lib.util.Utils;
import com.rs.lib.web.dto.CreateAccount;

public class Account {
	
	private String username;
	private String displayName;
	private String prevDisplayName = "";
	private String email;
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
	}
	
	public Account(String username, String password, String email) {
		this.username = Utils.formatPlayerNameForProtocol(username);
		this.displayName = Utils.formatPlayerNameForDisplay(username);
		this.prevDisplayName = "";
		this.email = email;
		this.rights = Rights.PLAYER;
		this.password = Crypt.encrypt(password);
		this.prevPasswords = new HashSet<>();
		this.prevPasswords.add(this.password);
		this.social = new Social();
	}
	
	public Account(CreateAccount request) {
		this(request.getUsername(), request.getPassword(), request.getEmail());
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

	public void setEmail(String email) {
		this.email = email;
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
	

	public boolean onlineTo(Account account) {
		return social.onlineTo(account);
	}

	public String getPrevDisplayName() {
		return prevDisplayName;
	}

	public void setPrevDisplayName(String prevDisplayName) {
		this.prevDisplayName = prevDisplayName;
	}
}
