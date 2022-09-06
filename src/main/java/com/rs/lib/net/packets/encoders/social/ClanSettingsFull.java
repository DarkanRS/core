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
package com.rs.lib.net.packets.encoders.social;

import java.util.Map;

import com.rs.cache.loaders.ClanVarSettingsDefinitions;
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Clan;
import com.rs.lib.model.DisplayNamePair;
import com.rs.lib.model.MemberData;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Logger;

public class ClanSettingsFull extends PacketEncoder {
	
	private boolean guest;
	private byte[] block;

	public ClanSettingsFull(byte[] block, boolean guest) {
		super(ServerPacket.CLANSETTINGS_FULL);
		this.block = block;
		this.guest = guest;
	}
	
	public static byte[] generateBlock(Clan clan, Map<String, DisplayNamePair> displayNameMap, int updateNum) {
		OutputStream stream = new OutputStream();
		int version = 3;
		stream.writeByte(version); // lowest clan version protocol
		stream.writeByte(0x2); // read name as string, 0x1 for long
		stream.writeInt(updateNum);
		stream.writeInt(0); // probably had same usage anyway doesnt anymore
		int fillerCount = 5 - clan.getMembers().size();
		if (fillerCount < 0)
			fillerCount = 0;
		stream.writeShort(clan.getMembers().size() + fillerCount);
		stream.writeByte(clan.getBannedUsers().size());
		stream.writeString(clan.getName());
		if (version >= 4)
			stream.writeInt(0);
		stream.writeByte(clan.isGuestsInChatCanEnter() ? 1 : 0);
		stream.writeByte(0); // talkRank
		stream.writeByte(clan.getMinimumRankForKick().getIconId());
		stream.writeByte(0); // lootshareRank
		stream.writeByte(0); // lootshareRank
		for (String username : clan.getMembers().keySet()) {
			MemberData data = clan.getMembers().get(username);
			// stream.writeLong(
			stream.writeString(displayNameMap.get(username).getCurrent());
			stream.writeByte(data.getRank().getIconId());
			if (version >= 2)
				stream.writeInt(0); // memberInt1
			if (version >= 5)
				stream.writeShort(0); // memberShort1
		}
		if (fillerCount > 0) {
			for (int i = 0;i < fillerCount;i++) {
				stream.writeString("FILLER_CHARACTER"+i);
				stream.writeByte(0);
				if (version >= 2)
					stream.writeInt(0);
				if (version >= 5)
					stream.writeShort(0);
			}
		}
		for (String bannedUser : clan.getBannedUsers()) {
			// stream.writeLong(bannedUser);
			stream.writeString(displayNameMap.get(bannedUser).getCurrent());
		}
		if (version >= 3) {
			int offset = stream.getOffset();
			stream.writeShort(0);
			int count = 0;
			for (int key : clan.getVars().keySet()) {
				if (writeClanVar(stream, key, clan.getVars().get(key)))
					count++;
			}
			int end = stream.getOffset();
			stream.setOffset(offset);
			stream.writeShort(count);
			stream.setOffset(end);
		}
		return stream.getBuffer();
	}
	
	public static boolean writeClanVar(OutputStream stream, int varId, Object value) {
		ClanVarSettingsDefinitions def = ClanVarSettingsDefinitions.getDefs(varId);
		if (def == null) {
			Logger.error(ClanSettingsFull.class, "writeClanVar", "No def found for clan var setting " + varId);
			return false;
		}
		if (def.type == CS2Type.INT) {
			stream.writeInt(varId | 0 << 30);
			stream.writeInt(value instanceof Double d ? d.intValue() : (int) value);
		} else if (def.type == CS2Type.LONG) {
			stream.writeInt(varId | 1 << 30);
			stream.writeLong(value instanceof Double d ? d.intValue() : (long) value);
		} else if (def.type == CS2Type.STRING) {
			stream.writeInt(varId | 2 << 30);
			stream.writeString((String) value);
		} else {
			Logger.error(ClanSettingsFull.class, "writeClanVar", "Unsupported var type " + def.type + " for var " + varId);
			return false;
		}
		return true;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(guest ? 0 : 1);
		if (block == null)
			return;
		stream.writeBytes(block);
	}
	
	public static final char[] VALID_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public static int getCharacterIndex(int c) {
		for (int i = 0; i < VALID_CHARS.length; i++)
			if (VALID_CHARS[i] == c)
				return i;
		return -1;
	}

	public static long convertToLong(String text) {
		long hash = 0;
		int count = text.length() - 1;
		for (char c : text.toCharArray()) {
			hash += getCharacterIndex(c) * Math.pow(36, count--);
		}
		return hash;
	}
}
