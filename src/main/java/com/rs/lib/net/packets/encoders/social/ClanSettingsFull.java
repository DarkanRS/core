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
import java.util.AbstractMap.SimpleEntry;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Clan;
import com.rs.lib.model.MemberData;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ClanSettingsFull extends PacketEncoder {
	
	private Clan clan;
	private Map<String, SimpleEntry<String, String>> displayNameMap;
	private boolean guest;
	private int updateNum;

	public ClanSettingsFull(Clan clan, Map<String, SimpleEntry<String, String>> displayNameMap, boolean guest, int updateNum) {
		super(ServerPacket.CLANSETTINGS_FULL);
		this.clan = clan;
		this.displayNameMap = displayNameMap;
		this.guest = guest;
		this.updateNum = updateNum;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(guest ? 0 : 1);
		if (clan == null)
			return;
		int version = 3;
		stream.writeByte(version); // lowest clan version protocol
		stream.writeByte(0x2); // read name as string, 0x1 for long
		stream.writeInt(updateNum);
		stream.writeInt(0); // probably had same usage anyway doesnt anymore
		stream.writeShort(clan.getMembers().size());
		stream.writeByte(clan.getBannedUsers().size());
		stream.writeString(clan.getName());
		if (version >= 4)
			stream.writeInt(0);
		stream.writeByte(clan.isGuestsInChatCanEnter() ? 1 : 0);
		stream.writeByte(1); // unknown
		stream.writeByte(0); // some rank for something in clan channel
		stream.writeByte(0); // unknown
		stream.writeByte(0); // unknown
		for (String username : clan.getMembers().keySet()) {
			MemberData data = clan.getMembers().get(username);
			// stream.writeLong(
			stream.writeString(displayNameMap.get(username).getKey());
			stream.writeByte(data.getRank().getIconId());
			if (version >= 2)
				stream.writeInt(0); // unknown
			if (version >= 5)
				stream.writeShort(0); // unknown
		}
		for (String bannedUser : clan.getBannedUsers()) {
			// stream.writeLong(bannedUser);
			stream.writeString(displayNameMap.get(bannedUser).getKey());
		}
		if (version >= 3) {
			int count = 2;
			if (clan.getTimeZone() != 0)
				count++;
			if (clan.getMotto() != null)
				count++;
			if (clan.getThreadId() != null)
				count++;
			if (clan.isRecruiting() || clan.isClanTime() || clan.getWorldId() != 0 || clan.getClanFlag() != 0)
				count++;
			if (clan.getMottifTop() != 0 || clan.getMottifBottom() != 0)
				count++;
			stream.writeShort(count); // configsCount
			if (clan.getTimeZone() != 0) {
				stream.writeInt(0 | 0 << 30);
				stream.writeInt(clan.getTimeZone());
			}
			if (clan.getMotto() != null) {
				stream.writeInt(1 | 2 << 30);
				stream.writeString(clan.getMotto());
			}
			if (clan.getThreadId() != null) {
				stream.writeInt(2 | 1 << 30);
				stream.writeLong(convertToLong(clan.getThreadId()));
			}
			if (clan.isRecruiting() || clan.isClanTime() || clan.getWorldId() != 0 || clan.getClanFlag() != 0) {
				stream.writeInt(3 | 0 << 30);
				stream.writeInt((clan.isRecruiting() ? 1 : 0) | (clan.isClanTime() ? 1 : 0) << 1 | clan.getWorldId() << 2 | clan.getClanFlag() << 10);
			}
			if (clan.getMottifTop() != 0 || clan.getMottifBottom() != 0) {
				stream.writeInt(13 | 0 << 30);
				stream.writeInt(clan.getMottifTop() | clan.getMottifBottom() << 16);
			}
			stream.writeInt(16 | 0 << 30);
			stream.writeInt(clan.getMottifColors()[0] | clan.getMottifColors()[1] << 16);
			stream.writeInt(18 | 0 << 30);
			stream.writeInt(clan.getMottifColors()[2] | clan.getMottifColors()[3] << 16);
		}
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
