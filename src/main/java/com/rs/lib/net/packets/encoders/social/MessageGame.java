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
import com.rs.lib.model.Account;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MessageGame extends PacketEncoder {
	
	private MessageType type;
	private String message;
	private Account target;
	
	public MessageGame(String message) {
		this(MessageType.UNFILTERABLE, message, null);
	}
	
	public MessageGame(MessageType type, String message) {
		this(type, message, null);
	}

	public MessageGame(MessageType type, String message, Account target) {
		super(ServerPacket.GAME_MESSAGE);
		this.type = type;
		this.message = message;
		this.target = target;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int maskData = 0;
		if (message.length() >= 248)
			message = message.substring(0, 248);

		if (target != null) {
			maskData |= 0x1;
			if (target.getDisplayName() != null)
				maskData |= 0x2;
		}
		stream.writeSmart(type.getValue());
		stream.writeInt(0); //junk
		stream.writeByte(maskData);
		if (target != null) {
			stream.writeString(target.getUsername());
			if (target.getDisplayName() != null)
				stream.writeString(target.getDisplayName());
		}
		stream.writeString(message);
	}

	public enum MessageType {
		UNFILTERABLE(0),
		STAFF_CHAT(1),
		PUBLIC_CHAT(2),
		PRIVATE_MESSAGE(3),
		ENGINE(4),
		FC_NOTIFICATION(11),
		ITEM_EXAMINE(27),
		NPC_EXAMINE(28),
		OBJECT_EXAMINE(29),
		FRIEND_NOTIFICATION(30),
		IGNORE_NOTIFICATION(31),
		DEV_CONSOLE_CLEAR(98),
		DEV_CONSOLE(99),
		TRADE_REQUEST(100),
		DUEL_REQUEST(101),
		ASSIST_REQUEST(102),
		UNK_103(103),
		UNK_104(104),
		UNK_CHALLENGE_105(105),
		UNK_CHALLENGE_106(106),
		UNK_CHALLENGE_107(107),
		ALLIANCE_REQUEST(108),
		FILTERABLE(109),
		UNK_GAME_110(110),
		DUNGEONEERING_INVITE(111),
		VOTE_REQUEST(112),
		UNK_CHALLENGE_113(113),
		UNK_CHALLENGE_114(114),
		UNK_DARK_RED_115(115),
		UNK_PLAIN_NOTIFICATION_116(116),
		CLAN_INVITE(117),
		CLAN_CHALLENGE_REQUEST(118),
		UNK_119(119),
		UNK_120(120);
		
		private int value;
		
		private MessageType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
}
