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
package com.rs.lib.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rs.cache.loaders.ClanVarSettingsDefinitions;
import com.rs.cache.loaders.EnumDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.lib.util.Logger;

public class Clan {
	
	public static final int MAX_MEMBERS = 100;

	private String clanLeaderUsername;
	private String name;
	private Map<String, MemberData> members;
	private Set<String> bannedUsers;
	private int timeZone;
	private boolean recruiting;
	private boolean isClanTime;
	private int worldId;
	private int clanFlag;
	private boolean guestsInChatCanEnter;
	private boolean guestsInChatCanTalk;
	private String threadId;
	private String motto;
	private int mottifTop, mottifBottom;
	private int[] mottifColors;
	private Map<Integer, Object> vars;

	private ClanRank minimumRankForKick;
	
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
		recruiting = true;
		guestsInChatCanEnter = true;
		guestsInChatCanTalk = true;
		minimumRankForKick = ClanRank.OWNER;
		worldId = 1;
		mottifColors = Arrays.copyOf(ItemDefinitions.getDefs(20709).modifiedModelColors, 4);
	}

	public MemberData addMember(Account account, ClanRank rank) {
		MemberData member = new MemberData(rank);
		members.put(account.getUsername(), member);
		return member;
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

	public int getTimeZone() {
		return timeZone;
	}

	public boolean isRecruiting() {
		return recruiting;
	}

	public void switchRecruiting() {
		recruiting = !recruiting;
	}

	public void setTimeZone(int gameTime) {
		this.timeZone = gameTime;
	}

	public ClanRank getMinimumRankForKick() {
		return minimumRankForKick;
	}

	public void setMinimumRankForKick(ClanRank minimumRankForKick) {
		this.minimumRankForKick = minimumRankForKick;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public boolean isGuestsInChatCanEnter() {
		return guestsInChatCanEnter;
	}

	public void switchGuestsInChatCanEnter() {
		this.guestsInChatCanEnter = !guestsInChatCanEnter;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public boolean isClanTime() {
		return isClanTime;
	}

	public void switchClanTime() {
		isClanTime = !isClanTime;
	}

	public int getWorldId() {
		return worldId;
	}

	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}

	public int getClanFlag() {
		return clanFlag;
	}

	public void setClanFlag(int clanFlag) {
		this.clanFlag = clanFlag;
	}

	public String getClanLeaderUsername() {
		return clanLeaderUsername;
	}

	public boolean isGuestsInChatCanTalk() {
		return guestsInChatCanTalk;
	}

	public int getMottifTop() {
		return mottifTop;
	}

	public void setMottifTop(int mottifTop) {
		this.mottifTop = mottifTop;
	}

	public int getMottifBottom() {
		return mottifBottom;
	}

	public void setMottifBottom(int mottifBottom) {
		this.mottifBottom = mottifBottom;
	}

	public int[] getMottifColors() {
		return mottifColors;
	}

	public void setMottifColours(int[] mottifColors) {
		this.mottifColors = mottifColors;
	}

	public void switchGuestsInChatCanTalk() {
		guestsInChatCanTalk = !guestsInChatCanTalk;
	}

	public int[] getMottifTextures() {
		return new int[] { getMottifTexture(getMottifTop()), getMottifTexture(getMottifBottom()) };
	}

	public static int getMottifSprite(int slotId) {
		return EnumDefinitions.getEnum(3686).getIntValue(slotId + 1);
	}

	public static int getMottifTexture(int slotId) {
		return EnumDefinitions.getEnum(3685).getIntValue(slotId + 1);
	}

	public void setMottifColour(int index, int colorId) {
		mottifColors[index] = colorId;
	}

	public byte[] getUpdateBlock() {
		return updateBlock;
	}

	public void setUpdateBlock(byte[] updateBlock) {
		this.updateBlock = updateBlock;
	}

	public Map<Integer, Object> getVars() {
		if (vars == null)
			vars = new HashMap<>();
		return vars;
	}
	
	public void setVarInt(int varId, int value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarInt", "No def found for clan var setting " + varId);
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
		vars.put(varId, value);
	}
	
	public void setVarBit(int varId, int value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarBit", "No def found for clan var setting " + varId);
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
        int orig = vars.get(def.baseVar) != null ? (int) vars.get(def.baseVar) : 0;
        orig &= ~mask;
        orig |= value;
		vars.put(def.baseVar, orig);
	}
	
	public void setVarLong(int varId, long value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarLong", "No def found for clan var setting " + varId);
			return;
		}
		if (def.type != CS2Type.LONG) {
			Logger.error(Clan.class, "setVarLong", "Tried to set long value for " + varId + " which is " + def.type);
			return;
		}
		vars.put(varId, value);
	}
	
	public void setVarString(int varId, String value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(Clan.class, "setVarLong", "No def found for clan var setting " + varId);
			return;
		}
		if (def.type != CS2Type.STRING) {
			Logger.error(Clan.class, "setVarLong", "Tried to set string value for " + varId + " which is " + def.type);
			return;
		}
		vars.put(varId, value);
	}
}
