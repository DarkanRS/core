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
