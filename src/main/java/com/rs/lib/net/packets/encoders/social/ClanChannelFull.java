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

import java.util.List;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.MemberData;
import com.rs.lib.model.MinimalSocial;
import com.rs.lib.model.clan.Clan;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ClanChannelFull extends PacketEncoder {
	
	private boolean guest;
	private byte[] block;

	public ClanChannelFull(byte[] block, boolean guest) {
		super(ServerPacket.CLANCHANNEL_FULL);
		this.guest = guest;
		this.block = block;
	}
	
	public static byte[] generateBlock(Clan clan, int updateNum, List<MinimalSocial> chatters) {
		OutputStream stream = new OutputStream();
		stream.writeByte(0x2); // read name as string, 0x1 for long
		stream.writeLong(4062231702422979939L); // uid
		stream.writeLong(updateNum);
		stream.writeString(clan.getName());
		stream.writeByte(0); // unused
		stream.writeByte(clan.getMinimumRankForKick().getIconId());
		stream.writeByte(clan.isGuestsInChatCanTalk() ? -1 : 0); // getMinimumRankForChat
		// maybe
		stream.writeShort(chatters.size());
		for (MinimalSocial player : chatters) {
			// stream.writeLong(); //Username as long
			MemberData data = clan.getMembers().get(player.getUsername());
			stream.writeString(player.getDisplayName());
			stream.writeByte(data == null ? -1 : data.getRank().getIconId());
			stream.writeShort(player.getWorld().getNumber());
		}
		return stream.getBuffer();
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(guest ? 0 : 1);
		if (block == null)
			return;
		stream.writeBytes(block);
	}

}
