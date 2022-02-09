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

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Account;
import com.rs.lib.model.Friend;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class FriendStatus extends PacketEncoder {
	
	private Account base;
	private Friend[] friends;

	public FriendStatus(Account base, Friend... friends) {
		super(ServerPacket.FRIEND_STATUS);
		this.base = base;
		this.friends = friends;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		for (Friend friend : friends)
			encodePlayer(base, friend, stream);
	}
	
	public void encodePlayer(Account player, Friend other, OutputStream stream) {
		boolean online = false;
		if (!other.isOffline() && player.onlineTo(other.getAccount()))
			online = true;
		stream.writeByte(0); //idk, was called warnMessage in matrix?
		stream.writeString(other.getAccount().getDisplayName());
		stream.writeString(other.getAccount().getPrevDisplayName());
		stream.writeShort(online ? other.getWorld() != null ? other.getWorld().getNumber() : 0 : 0);
		stream.writeByte(player.getSocial().getFriendsChat().getRank(other.getAccount().getUsername()).getId());
		stream.writeByte(0); // 1 = referrer
		if (online) {
			String worldText = "None";
			if (other.getWorld() != null) {
				if (other.getWorld().getNumber() > 1100)
					worldText = "Lobby " + (other.getWorld().getNumber() - 1100);
				else
					worldText = "World " + other.getWorld().getNumber();
			}
			stream.writeString(worldText);
			stream.writeByte(0); //Platform 0 = RS, 1 = Other (Quickchat related)
			stream.writeInt(0); //World flags?
		}
	}
}
