package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.game.GroundItem;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSubActiveGroundItem extends PacketEncoder {

	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;
	private GroundItem item;
	
	public IFOpenSubActiveGroundItem(int topId, int topChildId, int subId, boolean overlay, GroundItem item) {
		super(ServerPacket.IF_OPENSUB_ACTIVE_GROUNDITEM);
		this.topId = topId;
		this.topChildId = topChildId;
		this.subId = subId;
		this.overlay = overlay;
		this.item = item;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		
		stream.writeIntV2(topUid);
		stream.writeIntLE(xteas[2]);
		stream.writeShortLE(subId);
		stream.writeInt(xteas[3]);
		stream.writeIntV1(xteas[0]);
		stream.write128Byte(overlay ? 1 : 0);
		stream.writeIntV1((item.getTile().getPlane() << 28) | (item.getTile().getX() << 14) | item.getTile().getY());
		stream.writeIntV1(xteas[1]);
		stream.writeShortLE128(item.getId());
	}

}
