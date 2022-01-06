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
package com.rs.lib.net;

import java.util.HashMap;
import java.util.Map;

public enum ClientPacket {
	KEEPALIVE(0, 0),
	PLAYER_OP6(1, 3),
	SOUND_EFFECT_MUSIC_ENDED(2, 4),
	NPC_EXAMINE(3, 3),
	IF_ON_IF(4, 16),
	WORLD_MAP_CLICK(5, 4),
	PLAYER_OP2(6, 3),
	FC_SET_RANK(7, -1),
	GROUND_ITEM_OP4(8, 7),
	IF_OP4(9, 8),
	SEND_PREFERENCES(10, -1),
	RESUME_HSLDIALOG(11, 2),
	REMOVE_IGNORE(12, -1),
	IF_ON_PLAYER(13, 11),
	QUICKCHAT_PRIVATE(14, -1),
	SEND_PRIVATE_MESSAGE(15, -2),
	NPC_OP2(16, 3),
	GE_ITEM_SELECT(17, 2),
	SONG_LOADED(18, 4),
	IF_OP6(19, 8),
	CHAT_SETFILTER(20, 3),
	IF_OP8(21, 8),
	IF_OP9(22, 8),
	IF_OP7(23, 8),
	GROUND_ITEM_OP1(24, 7),
	GROUND_ITEM_OP2(25, 7),
	ADD_FRIEND(26, -1),
	IF_OP2(27, 8),
	KEY_PRESS(28, -2),
	REMOVE_FRIEND(29, -1),
	CHAT_TYPE(30, 1),
	PLAYER_OP3(31, 3),
	OBJECT_OP4(32, 9),
	WALK(33, 5),
	ADD_IGNORE(34, -1),
	BUG_REPORT(35, -2),
	REFLECTION_CHECK(36, -1),
	UNK_37(37, 2), //index 36 and some hook/click type related
	OBJECT_OP3(38, 9),
	UNUSED_CLAN_OP(39, -1), //Packet is referenced by one CS2 instruction but that CS2 instruction is present in no scripts
	MOVE_MOUSE(40, -1),
	IF_ON_NPC(41, 11),
	MINI_WALK(42, 18),
	GROUND_ITEM_OP5(43, 7),
	SEND_FPS(44, 9),
	CUTSCENE_FINISHED(45, 1),
	IF_ON_TILE(46, 12),
	REQUEST_WORLD_LIST(47, 4),
	OBJECT_OP5(48, 9),
	IF_CONTINUE(49, 6),
	NPC_OP3(50, 3),
	PLAYER_OP7(51, 3),
	EMAIL_VALIDATION_SUBMIT_CODE(52, -1),
	PLAYER_OP9(53, 3),
	GROUND_ITEM_OP3(54, 7),
	TRANSMITVAR_VERIFYID(55, 4),
	LOBBY_HYPERLINK(56, -2),
	MOUSE_BUTTON_CLICK(57, 7),
	RESUME_COUNTDIALOG(58, 4),
	MOUSE_CLICK(59, 6),
	CLOSE_INTERFACE(60, 0),
	GROUND_ITEM_EXAMINE(61, 7),
	CLIENT_FOCUS(62, 1),
	UNK_63(63, 4), //near where shift click teleporting so some kind of map click probably?
	QUICKCHAT_PUBLIC(64, -1),
	NPC_OP1(65, 3),
	PLAYER_OP1(66, 3),
	IF_ON_GROUND_ITEM(67, 15),
	IF_OP3(68, 8),
	RESUME_CLANFORUMQFCDIALOG(69, -1),
	PLAYER_OP10(70, 3),
	FC_JOIN(71, -1),
	IF_OP5(72, 8),
	OBJECT_EXAMINE(73, 9),
	IF_DRAG_ONTO_IF(74, 16),
	OBJECT_OP1(75, 9),
	REGION_LOADED_CONFIRM(76, 0),
	NPC_OP4(77, 3),
	MOVE_MOUSE_2(78, -1),
	UNK_79(79, 1), //TODO account creation related
	RESUME_NAMEDIALOG(80, -1),
	IF_OP10(81, 8),
	UNK_82(82, 4), //writes one identical int sometime during gamestate/region loading. Probably sends when something fails to load or error occurs
	MOVE_CAMERA(83, 4),
	SCREEN_SIZE(84, 6),
	CLIENT_CHEAT(85, -1),
	CHAT(86, -1),
	RESUME_TEXTDIALOG(87, -1),
	WRITE_PING(88, 2),
	PLAYER_OP4(89, 3),
	CLANCHANNEL_KICKUSER(90, -1),
	FC_KICK(91, -1),
	EMAIL_VALIDATION_ADD_NEW_ADDRESS(92, -2),
	OBJECT_OP2(93, 9),
	PLAYER_OP8(94, 3),
	NPC_OP5(95, 3),
	IF_OP1(96, 8),
	UNK_97(97, -1), //cs2 driven something during login stage?
	IF_ON_OBJECT(98, 17),
	EMAIL_VALIDATION_CHANGE_ADDRESS(99, -2),
	REPORT_ABUSE(100, -1),
	SEND_SIGN_UP_FORM(101, -2),
	CHECK_EMAIL_VALIDITY(102, -2),
	PLAYER_OP5(103, 3),
	
	//CUSTOM OPCODES FOR LOBBY
	CC_JOIN(180, 0),
	CC_LEAVE(181, 0),
	CC_INVITE(182, 0),
	CC_BAN(183, 0),
	CC_CREATE(184, 0)
	;

	private int id;
	private int size;
	
	private static Map<Integer, ClientPacket> PACKET_MAP = new HashMap<>();
	
	static {
		for (ClientPacket packet : ClientPacket.values()) {
			PACKET_MAP.put(packet.id, packet);
		}
	}
	
	public static ClientPacket forOpcode(int opcode) {
		return PACKET_MAP.get(opcode);
	}

	ClientPacket(int id, int size) {
		this.id = id;
		this.size = size;
	}
	
	public int getOpcode() {
		return id;
	}
	
	public int getSize() {
		return size;
	}
}
