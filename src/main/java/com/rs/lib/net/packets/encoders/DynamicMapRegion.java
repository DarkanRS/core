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
