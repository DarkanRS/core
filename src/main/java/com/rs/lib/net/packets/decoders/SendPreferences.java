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
package com.rs.lib.net.packets.decoders;

import java.util.HashMap;
import java.util.Map;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.SEND_PREFERENCES)
public class SendPreferences extends Packet {
	
	public enum Preference {
		ANTI_ALIASING(1),
		BLOOM(2),
		BRIGHTNESS(3),
		UNK4(4),
		UNK5(5),
		UNK6(6),
		FOG(7),
		UNK8(8),
		GROUND_DECORATION(9),
		IDLE_ANIMATIONS(10),
		LIGHT_DETAIL(11),
		SCENERY_SHADOWS(12),
		UNK13(13),
		PARTICLES(14),
		UNK15(15),
		UNK16(16),
		UNK17(17),
		UNK18(18),
		TEXTURES(19),
		UNK20(20),
		WATER(22),
		SCREEN_SIZE(23),
		CUSTOM_CURSORS(24),
		GRAPHICS(25),
		CPU(26),
		UNK27(27),
		SAFE_MODE(28),
		UNK29(29),
		SOUND_EFFECT_VOL(30),
		AMBIENT_SOUND_VOL(31),
		VOICE_VOL(32),
		MUSIC_VOL(33),
		UNK34(34),
		MONO_STEREO(35);
		
		private static Map<Integer, Preference> MAP = new HashMap<>();
		
		static {
			for (Preference p : Preference.values())
				MAP.put(p.index, p);
		}
		
		public static Preference forIndex(int index) {
			return MAP.get(index);
		}
		
		private int index;
		
		private Preference(int index) {
			this.index = index;
		}
	}
	
	private Map<Preference, Integer> preferences = new HashMap<>();

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		SendPreferences s = new SendPreferences();
		int currIdx = 0;
		while(stream.getRemaining() > 0) {
			Preference p = Preference.forIndex(currIdx++);
			if (p != null)
				s.preferences.put(p, stream.readUnsignedByte());
			else
				stream.readUnsignedByte();
		}
		return s;
	}

}
