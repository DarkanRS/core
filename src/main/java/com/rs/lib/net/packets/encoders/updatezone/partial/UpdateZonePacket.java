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

