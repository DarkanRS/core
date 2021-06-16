package com.rs.lib.net.packets.decoders;

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.WORLD_MAP_CLICK)
public class WorldMapClick extends Packet {
	
	private WorldTile tile;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		WorldMapClick packet = new WorldMapClick();
		int coordinateHash = stream.readIntLE();
		packet.tile = new WorldTile(coordinateHash >> 14, coordinateHash & 0x3fff, coordinateHash >> 28);
		return packet;
	}

	public WorldTile getTile() {
		return tile;
	}

}
