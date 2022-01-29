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
