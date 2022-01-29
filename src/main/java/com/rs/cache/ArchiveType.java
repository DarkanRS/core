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
package com.rs.cache;

public enum ArchiveType {
	UNDERLAYS(1),
	SCT_2(2),
	IDENTIKIT(3),
	OVERLAYS(4),
	INVENTORIES(5),
	OBJECTS(6, 8),
	SCT_7(7),
	ENUMS(8, 8),
	NPCS(9, 7),
	ITEMS(10, 8),
	PARAMS(11),
	ANIMATIONS(12, 7),
	SPOT_ANIMS(13, 8),
	VARBITS(14, 10),
	VARC_STRING(15),
	VARS(16),
	SCT_17(17),
	SCT_18(18),
	VARC(19),
	SCT_20(20),
	SCT_21(21),
	SCT_22(22),
	SCT_23(23),
	SCT_24(24), //varn
	SCT_25(25), //varnBit
	STRUCTS(26),
	SCT_27(27),
	SCT_28(28),
	SKYBOX(29),
	SUN(30),
	LIGHT_INTENSITIES(31),
	BAS(32),
	CURSORS(33),
	MAP_SPRITES(34),
	QUESTS(35),
	MAP_AREAS(36),
	SCT_37(37),
	SCT_38(38),
	SCT_39(39),
	SCT_40(40),
	SCT_41(41),
	SCT_42(42),
	SCT_43(43),
	SCT_44(44),
	SCT_45(45),
	HITSPLATS(46),
	CLAN_VAR(47),
	SCT_48(48),
	SCT_49(49),
	SCT_50(50),
	SCT_51(51),
	SCT_53(53),
	CLAN_VAR_SETTINGS(54),
	SCT_70(70),
	HITBARS(72),
	SCT_73(73),
	SCT_74(74);
	
	private int id, fileIdBitShift;
	
	private ArchiveType(int id) {
		this(id, 0);
	}
	
	ArchiveType(int id, int fileIdBitShift) {
		this.id = id;
		this.fileIdBitShift = fileIdBitShift;
	}
	
	public int getId() {
		return id;
	}
	
	public int filesPerContainer() {
		return 1 << this.fileIdBitShift;
	}

	public int archiveId(int id) {
		return id >>> this.fileIdBitShift;
	}

	public int fileId(int id) {
		return id & (1 << this.fileIdBitShift) - 1;
	}
}
