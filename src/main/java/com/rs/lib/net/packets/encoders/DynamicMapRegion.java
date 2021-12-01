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
package com.rs.lib.net.packets.encoders;

import com.rs.cache.loaders.map.RegionSize;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.MapXTEAs;

public class DynamicMapRegion extends PacketEncoder {
	
	private byte[] lswp;
	private RegionSize mapSize;
	private int chunkX, chunkY;
	private boolean forceMapRefresh;
	private byte[] dynamicBytes;
	private int[] realRegionIds;

	public DynamicMapRegion(byte[] lswp, RegionSize mapSize, int chunkX, int chunkY, boolean forceMapRefresh, byte[] dynamicBytes, int[] realRegionIds) {
		super(ServerPacket.DYNAMIC_MAP_REGION);
		this.lswp = lswp;
		this.mapSize = mapSize;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.forceMapRefresh = forceMapRefresh;
		this.dynamicBytes = dynamicBytes;
		this.realRegionIds = realRegionIds;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (lswp != null)
			stream.writeBytes(lswp);
		int regionX = chunkX;
		int regionY = chunkY;
		stream.writeByteC(forceMapRefresh ? 1 : 0);
		stream.write128Byte(2);
		stream.writeByte128(mapSize.ordinal());
		stream.writeShort128(regionY);
		stream.writeShort(regionX);
		stream.writeBytes(dynamicBytes);
		for (int index = 0; index < realRegionIds.length; index++) {
			int[] xteas = MapXTEAs.getMapKeys(realRegionIds[index]);
			if (xteas == null)
				xteas = new int[4];
			for (int keyIndex = 0; keyIndex < 4; keyIndex++)
				stream.writeInt(xteas[keyIndex]);
		}
	}

}
