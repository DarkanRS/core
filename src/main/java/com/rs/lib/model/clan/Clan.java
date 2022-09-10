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
package com.rs.lib.model.clan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rs.cache.loaders.ClanVarDefinitions;
import com.rs.cache.loaders.ClanVarSettingsDefinitions;
import com.rs.cache.loaders.EnumDefinitions;
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.lib.model.Account;
import com.rs.lib.model.MemberData;
import com.rs.lib.util.Logger;

public class Clan {
	
	public static final int MAX_MEMBERS = 100;

	private String clanLeaderUsername;
	private String name;
	private Map<String, MemberData> members;
	private Set<String> bannedUsers;
	private Map<Integer, Object> settings;
	private Map<Integer, Object> vars;

	private ClanRank ccChatRank;
	private ClanRank ccKickRank;
	
	private byte[] updateBlock;

	public Clan(String name, Account leader) {
		this.name = name;
		setDefaults();
		this.members = new HashMap<>();
		this.bannedUsers = new HashSet<>();
		addMember(leader, ClanRank.OWNER);
		setClanLeaderUsername(leader.getUsername());
	}

	public void setDefaults() {
		settings.clear();
		vars.clear();
		setCcChatRank(ClanRank.NONE);
		setCcKickRank(ClanRank.ADMIN);
		for (ClanSetting setting : ClanSetting.values())
			if (setting.getDefault() != null)
				setSetting(setting, setting.getDefault());
	}
	
	public void setMotifColor(int[] colors) {
		if (colors.length != 4) {
			Logger.error(Clan.class, "setMotifColor", "Motif colors must be an array of size 4!");
			return;
		}
		for (int i = 0;i < colors.length;i++)
			setMotifColor(i, colors[i]);
	}
	
	public void setMotifColor(int index, int color) {
		switch(index) {
		case 0 -> setSetting(ClanSetting.MOTIF_TOP_COLOR, color);
		case 1 -> setSetting(ClanSetting.MOTIF_BOTTOM_COLOR, color);
		case 2 -> setSetting(ClanSetting.MOTIF_PRIMARY_COLOR, color);
		case 3 -> setSetting(ClanSetting.MOTIF_SECONDARY_COLOR, color);
		}
	}
	
	public int getMotifTopIcon() {
		return (int) getSetting(ClanSetting.MOTIF_TOP_ICON);
	}
	
	public int getMotifBottomIcon() {
		return (int) getSetting(ClanSetting.MOTIF_BOTTOM_ICON);
	}
	
	public int[] getMotifColors() {
		return new int[] { (int) getSetting(ClanSetting.MOTIF_TOP_COLOR), (int) getSetting(ClanSetting.MOTIF_BOTTOM_COLOR), (int) getSetting(ClanSetting.MOTIF_PRIMARY_COLOR), (int) getSetting(ClanSetting.MOTIF_SECONDARY_COLOR) };
	}

	public MemberData addMember(Account account, ClanRank rank) {
		MemberData member = new MemberData(rank);
		members.put(account.getUsername(), member);
		return member;
	}
	
	public void removeMember(String username) {
		members.remove(username);
	}

	public void setClanLeaderUsername(String username) {
		clanLeaderUsername = username;
	}

	public Map<String, MemberData> getMembers() {
		return members;
	}

	public Set<String> getBannedUsers() {
		return bannedUsers;
	}

	public String getName() {
		return name;
	}

	public void switchRecruiting() {
		setSetting(ClanSetting.IS_RECRUITING, (int) getSetting(ClanSetting.IS_RECRUITING) == 0 ? 1 : 0);
	}

	public String getClanLeaderUsername() {
		return clanLeaderUsername;
	}

	public void setMotto(String motto) {
		setSetting(ClanSetting.MOTTO, motto);
	}

	public int[] getMotifTextures() {
		return new int[] { getMotifTexture((int) getSetting(ClanSetting.MOTIF_TOP_ICON)), getMotifTexture((int) getSetting(ClanSetting.MOTIF_BOTTOM_ICON)) };
	}
	
	public static int getMotifTexture(int slotId) {
		return EnumDefinitions.getEnum(3685).getIntValue(slotId);
	}

	public byte[] getUpdateBlock() {
		return updateBlock;
	}

	public void setUpdateBlock(byte[] updateBlock) {
		this.updateBlock = updateBlock;
	}

	public Map<Integer, Object> getSettings() {
		if (settings == null) {
			settings = new HashMap<>();
			setDefaults();
		}
		return settings;
	}
	
	public Object getSetting(ClanSetting setting) {
		return getSetting(setting.getId());
	}
	
	public Object getSetting(int id) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(id);
		try {
			boolean varBit = def.baseVar != -1;
			if (varBit) {
				return getSettingBit(id);
			} else if (def.type == CS2Type.LONG) {
				if (getSettings().get(id) == null)
					return 0;
				if (getSettings().get(id) instanceof Double d)
					return d.intValue();
				return (long) getSettings().get(id);
			} else if (def.type == CS2Type.INT) {
				if (getSettings().get(id) == null)
					return 0;
				if (getSettings().get(id) instanceof Double d)
					return d.intValue();
				return (int) getSettings().get(id);
			} else if (def.type == CS2Type.STRING) {
				if (getSettings().get(id) == null)
					return null;
				return (String) getSettings().get(id);
			}
		} catch(Throwable e) {
			Logger.handle(Clan.class, "getSetting", e);
		}
		return null;
	}
	
    public int getSettingBit(ClanSetting setting) {
		return getSettingBit(setting.getId());
	}
    
    public int getSettingBit(int id) {
    	ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(id);
    	if (def.baseVar == -1) {
    		Logger.error(Clan.class, "getSettingBit", "Setting " + id + " is not a varbit.");
			return 0;
    	}
		int val = (int) getSetting(def.baseVar);
		int endMask = def.endBit == 31 ? -1 : (1 << def.endBit + 1) - 1;
		return Integer.valueOf((val & endMask) >>> def.startBit);
	}
	
	public void setSetting(ClanSetting setting, Object value) {
		try {
			boolean varBit = setting.getDef().baseVar != -1;
			if (varBit) {
				setSettingBit(setting.getId(), (int) value);
			} else if (setting.getDef().type == CS2Type.LONG) {
				setSettingLong(setting.getId(), (long) value);
			} else if (setting.getDef().type == CS2Type.INT) {
				setSettingInt(setting.getId(), (int) value);
			} else if (setting.getDef().type == CS2Type.STRING) {
				setSettingString(setting.getId(), (String) value);
			}
		} catch(Throwable e) {
			Logger.handle(Clan.class, "setSetting", e);
		}
	}
	
	public void setSettingInt(int varId, int value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setSettingInt", "No def found for clan setting " + varId);
			return;
		}
		if (def.type != CS2Type.INT) {
			Logger.error(Clan.class, "setSettingInt", "Tried to set int value for " + varId + " which is " + def.type);
			return;
		}
		if (def.baseVar != -1) {
			Logger.error(Clan.class, "setSettingInt", "Setting " + varId + " should be a varbit.");
			return;
		}
		getSettings().put(varId, value);
	}
	
	public void setSettingBit(int varId, int value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setSettingBit", "No def found for clan var setting " + varId);
			return;
		}
		if (def.type != CS2Type.INT) {
			Logger.error(Clan.class, "setSettingBit", "Tried to set int value for " + varId + " which is " + def.type);
			return;
		}
		if (def.baseVar == -1) {
			Logger.error(Clan.class, "setSettingBit", "Var " + varId + " is not a valid varbit.");
			return;
		}
		int startMask = (1 << def.startBit) - 1;
        int endMask = def.endBit == 31 ? -1 : (1 << def.endBit + 1) - 1;
        int mask = endMask ^ startMask;
        value <<= def.startBit;
        value &= mask;
        int orig = (int) getSetting(def.baseVar);
        orig &= ~mask;
        orig |= value;
        getSettings().put(def.baseVar, orig);
	}
	
	public void setSettingLong(int varId, long value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setSettingLong", "No def found for clan var setting " + varId);
			return;
		}
		if (def.type != CS2Type.LONG) {
			Logger.error(Clan.class, "setSettingLong", "Tried to set long value for " + varId + " which is " + def.type);
			return;
		}
		getSettings().put(varId, value);
	}
	
	public void setSettingString(int varId, String value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setSettingString", "No def found for clan var setting " + varId);
			return;
		}
		if (def.type != CS2Type.STRING) {
			Logger.error(Clan.class, "setSettingString", "Tried to set string value for " + varId + " which is " + def.type);
			return;
		}
		if (value.length() > 80)
			value = value.substring(0, 80);
		getSettings().put(varId, value);
	}

	public Map<Integer, Object> getVars() {
		if (vars == null)
			vars = new HashMap<>();
		return vars;
	}
	
	public Object getVar(ClanVar var) {
		return getVar(var.getId());
	}
	
	public Object getVar(int id) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(id);
		try {
			boolean varBit = def.baseVar != -1;
			if (varBit) {
				return getVarBit(id);
			} else if (def.type == CS2Type.LONG) {
				if (getVars().get(id) == null)
					return 0;
				if (getVars().get(id) instanceof Double d)
					return d.intValue();
				return (long) getVars().get(id);
			} else if (def.type == CS2Type.INT) {
				if (getVars().get(id) == null)
					return 0;
				if (getVars().get(id) instanceof Double d)
					return d.intValue();
				return (int) getVars().get(id);
			} else if (def.type == CS2Type.STRING) {
				if (getVars().get(id) == null)
					return null;
				return (String) getVars().get(id);
			}
		} catch(Throwable e) {
			Logger.handle(Clan.class, "getVar", e);
		}
		return null;
	}
	
	public boolean hasPermissions(String username, ClanRank rank) {
		MemberData data = members.get(username);
		if (data == null)
			return false;
		return data.getRank().ordinal() >= rank.ordinal();
	}
	
	public boolean hasPermissions(String username, ClanPermission permission) {
		return permission.hasPermission(this, getRank(username));
	}
	
	public ClanRank getRank(String username) {
		MemberData data = members.get(username);
		if (data == null)
			return ClanRank.NONE;
		return data.getRank();
	}
	
    public int getVarBit(ClanVar var) {
		return getVarBit(var.getId());
	}
    
    public int getVarBit(int id) {
    	ClanVarDefinitions def = ClanVarDefinitions.getDefs(id);
    	if (def.baseVar == -1) {
    		Logger.error(Clan.class, "getVarBit", "Var " + id + " is not a varbit.");
			return 0;
    	}
		int val = (int) getVar(def.baseVar);
		int endMask = def.endBit == 31 ? -1 : (1 << def.endBit + 1) - 1;
		return Integer.valueOf((val & endMask) >>> def.startBit);
	}
	
	public void setVar(ClanVar var, Object value) {
		try {
			setVar(var.getId(), value);
		} catch(Throwable e) {
			Logger.handle(Clan.class, "setVar", e);
		}
	}
	
	public void setVar(int var, Object value) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(var);
		try {
			boolean varBit = def.baseVar != -1;
			if (varBit) {
				setVarBit(var, (int) value);
			} else if (def.type == CS2Type.LONG) {
				setVarLong(var, (long) value);
			} else if (def.type == CS2Type.INT) {
				setVarInt(var, (int) value);
			} else if (def.type == CS2Type.STRING) {
				setVarString(var, (String) value);
			}
		} catch(Throwable e) {
			Logger.handle(Clan.class, "setVar", e);
		}
	}
	
	public void setVarInt(int varId, int value) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarInt", "No def found for clan var " + varId);
			return;
		}
		if (def.type != CS2Type.INT) {
			Logger.error(Clan.class, "setVarInt", "Tried to set int value for " + varId + " which is " + def.type);
			return;
		}
		if (def.baseVar != -1) {
			Logger.error(Clan.class, "setVarBit", "Var " + varId + " should be a varbit.");
			return;
		}
		getVars().put(varId, value);
	}
	
	public void setVarBit(int varId, int value) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarBit", "No def found for clan var " + varId);
			return;
		}
		if (def.type != CS2Type.INT) {
			Logger.error(Clan.class, "setVarBit", "Tried to set int value for " + varId + " which is " + def.type);
			return;
		}
		if (def.baseVar == -1) {
			Logger.error(Clan.class, "setVarBit", "Var " + varId + " is not a valid varbit.");
			return;
		}
		int startMask = (1 << def.startBit) - 1;
        int endMask = def.endBit == 31 ? -1 : (1 << def.endBit + 1) - 1;
        int mask = endMask ^ startMask;
        value <<= def.startBit;
        value &= mask;
        int orig = (int) getVar(def.baseVar);
        orig &= ~mask;
        orig |= value;
        getVars().put(def.baseVar, orig);
	}
	
	public void setVarLong(int varId, long value) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarLong", "No def found for clan var " + varId);
			return;
		}
		if (def.type != CS2Type.LONG) {
			Logger.error(Clan.class, "setVarLong", "Tried to set long value for " + varId + " which is " + def.type);
			return;
		}
		getVars().put(varId, value);
	}
	
	public void setVarString(int varId, String value) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarLong", "No def found for clan var " + varId);
			return;
		}
		if (def.type != CS2Type.STRING) {
			Logger.error(Clan.class, "setVarLong", "Tried to set string value for " + varId + " which is " + def.type);
			return;
		}
		if (value.length() > 80)
			value = value.substring(0, 80);
		getVars().put(varId, value);
	}

	public ClanRank getCcChatRank() {
		if (ccChatRank == null)
			setDefaults();
		return ccChatRank;
	}

	public void setCcChatRank(ClanRank ccChatRank) {
		this.ccChatRank = ccChatRank;
	}

	public ClanRank getCcKickRank() {
		if (ccChatRank == null)
			setDefaults();
		return ccKickRank;
	}

	public void setCcKickRank(ClanRank ccKickRank) {
		this.ccKickRank = ccKickRank;
	}
}
