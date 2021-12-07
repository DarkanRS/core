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
package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Clan;
import com.rs.lib.model.ClanMember;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ClanChannelFull extends PacketEncoder {
	
	private Clan clan;
	private boolean guest;
	private int updateNum;
	private ClanMember[] members;

	public ClanChannelFull(Clan clan, boolean guest, int updateNum, ClanMember... members) {
		super(ServerPacket.CLANCHANNEL_FULL);
		this.clan = clan;
		this.guest = guest;
		this.updateNum = updateNum;
		this.members = members;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(guest ? 0 : 1);
		if (clan == null)
			return;
		stream.writeByte(0x2); // read name as string, 0x1 for long
		stream.writeLong(4062231702422979939L); // uid
		stream.writeLong(updateNum);
		stream.writeString(clan.getName());
		stream.writeByte(0); // unused
		stream.writeByte(clan.getMinimumRankForKick());
		stream.writeByte(clan.isGuestsInChatCanTalk() ? -1 : 0); // getMinimumRankForChat
		// maybe
		stream.writeShort(members.length);
		for (ClanMember player : members) {
			// stream.writeLong(
			stream.writeString(player.getUsername());
			stream.writeByte(player.getRank().getIconId());
			stream.writeShort(1); // worldId
		}
	}

}
