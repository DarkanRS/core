package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class TileMessage extends PacketEncoder {

	private WorldTile tile;
	private String message;
	private int delay, height, color;
	
	public TileMessage(WorldTile tile, String message, int delay, int height, int color) {
		super(ServerPacket.TILE_MESSAGE);
		this.tile = tile;
		this.message = message;
		this.delay = delay;
		this.height = height;
		this.color = color;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.skip(1);
		stream.writeByte((tile.getXInChunk() << 4) | tile.getYInChunk());
		stream.writeShort(delay / 30);
		stream.writeByte(height);
		stream.write24BitInteger(color);
		stream.writeString(message);
	}

}
