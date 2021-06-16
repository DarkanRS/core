package com.rs.lib.net.packets.encoders.updatezone.partial;

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateZonePartialEnclosed extends PacketEncoder {
	
	private WorldTile tile;
	private int sceneBaseChunkId;
	private UpdateZoneEvent[] events;
	
	public UpdateZonePartialEnclosed(WorldTile tile, int sceneBaseChunkId, UpdateZoneEvent... events) {
		super(ServerPacket.UPDATE_ZONE_PARTIAL_ENCLOSED);
		this.events = events;
		this.sceneBaseChunkId = sceneBaseChunkId;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.write128Byte(tile.getYInScene(sceneBaseChunkId) >> 3);
		stream.writeByte128(tile.getPlane());
		stream.writeByte(tile.getXInScene(sceneBaseChunkId) >> 3);
		for (UpdateZoneEvent event : events) {
			stream.writeByte(event.getPacket().ordinal());
			event.getEncoder().encodeBody(stream);
		}
	}

}
