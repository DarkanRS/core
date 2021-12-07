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
package com.rs.cache;

public enum IndexType {
	ANIMATION_FRAME_SETS(0),
	ANIMATION_FRAME_BASES(1),
	CONFIG(2),
	INTERFACES(3),
	SOUND_EFFECTS(4),
	MAPS(5),
	MUSIC(6),
	MODELS(7),
	SPRITES(8),
	MATERIALS(9),
	HUFFMAN(10),
	MUSIC2(11),
	CS2_SCRIPTS(12),
	FONT_METRICS(13),
	MIDI_INSTRUMENTS(14),
	SOUND_EFFECTS_MIDI(15),
	OBJECTS(16),
	ENUMS(17),
	NPCS(18),
	ITEMS(19),
	ANIMATIONS(20),
	SPOT_ANIMS(21),
	VARBITS(22),
	MAP_AREAS(23),
	QC_MESSAGES(24),
	QC_MENUS(25),
	TEXTURES(26),
	PARTICLES(27),
	DEFAULTS(28),
	BILLBOARDS(29),
	NATIVE_LIBRARIES(30),
	SHADERS(31),
	NORMAL_FONTS(32),
	GAME_TIPS(33),
	JAGEX_FONTS(34),
	CUTSCENES(35),
	VORBIS(36);
	
	private int id;
	
	private IndexType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
