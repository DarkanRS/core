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
package com.rs.lib.net.packets.encoders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.rs.cache.loaders.map.RegionSize;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.MapXTEAs;

public class MapRegion extends PacketEncoder {
	
	private byte[] lswp;
	private RegionSize mapSize;
	private int chunkX, chunkY;
	private boolean forceMapRefresh;
	private Set<Integer> regionIds;

	public MapRegion(byte[] lswp, RegionSize mapSize, int chunkX, int chunkY, boolean forceMapRefresh, Set<Integer> regionIds) {
		super(ServerPacket.REGION);
		this.lswp = lswp;
		this.mapSize = mapSize;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.forceMapRefresh = forceMapRefresh;
		this.regionIds = regionIds;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (lswp != null)
			stream.writeBytes(lswp);
		stream.writeByte(mapSize.ordinal());
		stream.writeShort(chunkX);
		stream.writeShort(chunkY);
		stream.writeByte(forceMapRefresh ? 1 : 0);
		List<Integer> regionList = new ArrayList<>(regionIds);
		Collections.sort(regionList);
		for (int regionId : regionList) {
			int[] xteas = MapXTEAs.getMapKeys(regionId);
			if (xteas == null)
				xteas = new int[] { 0, 0, 0, 0 };
			for (int index = 0; index < 4; index++)
				stream.writeInt(xteas[index]);
		}
	}

}
