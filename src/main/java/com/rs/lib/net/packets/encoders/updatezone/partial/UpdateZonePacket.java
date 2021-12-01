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
package com.rs.lib.net.packets.encoders.updatezone.partial;

import com.rs.lib.net.ServerPacket;

public enum UpdateZonePacket {
    CUSTOMIZE_OBJECT(ServerPacket.CUSTOMIZE_OBJECT, -1),
    REMOVE_GROUND_ITEM(ServerPacket.REMOVE_GROUND_ITEM, 3),
    CREATE_GROUND_ITEM(ServerPacket.CREATE_GROUND_ITEM, 5),
    DESTROY_OBJECT(ServerPacket.DESTROY_OBJECT, 2),
    CREATE_OBJECT(ServerPacket.CREATE_OBJECT, 6),
    MAP_PROJANIM(ServerPacket.MAP_PROJANIM, 16),
    MAP_PROJANIM_HALFSQ(ServerPacket.MAP_PROJANIM_HALFSQ, 19),
    OBJECT_PREFETCH(ServerPacket.OBJECT_PREFETCH, 5),
    GROUND_ITEM_REVEAL(ServerPacket.GROUND_ITEM_REVEAL, 7),
    GROUND_ITEM_COUNT(ServerPacket.GROUND_ITEM_COUNT, 7),
    MIDI_SONG_LOCATION(ServerPacket.MIDI_SONG_LOCATION, 8),
    TILE_MESSAGE(ServerPacket.TILE_MESSAGE, -1),
    OBJ_ANIM(ServerPacket.OBJ_ANIM, 6),
    SOUND_AREA(null, 9),
    SPOT_ANIM(ServerPacket.SPOT_ANIM, 8);
	
	private ServerPacket serverPacket;
	private int size;

    UpdateZonePacket(ServerPacket serverPacket, int size) {
    	this.serverPacket = serverPacket;
    	this.size = size;
    }
    
    public int getSize() {
    	return size;
    }
    
    public ServerPacket getServerPacket() {
    	return serverPacket;
    }
}

