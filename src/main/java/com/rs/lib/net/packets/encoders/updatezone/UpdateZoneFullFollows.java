package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateZoneFullFollows extends PacketEncoder {
	
	private WorldTile tile;
	private int sceneBaseChunkId;
	
	public UpdateZoneFullFollows(WorldTile tile, int sceneBaseChunkId) {
		super(ServerPacket.UPDATE_ZONE_FULL_FOLLOWS);
		this.tile = tile;
		this.sceneBaseChunkId = sceneBaseChunkId;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte128(tile.getYInScene(sceneBaseChunkId) >> 3);
		stream.writeByte128(tile.getPlane());
		stream.writeByte128(tile.getXInScene(sceneBaseChunkId) >> 3);
	}

}
