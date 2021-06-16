package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.game.WorldObject;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSubActiveObject extends PacketEncoder {

	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;
	private WorldObject object;
	
	public IFOpenSubActiveObject(int topId, int topChildId, int subId, boolean overlay, WorldObject object) {
		super(ServerPacket.IF_OPENSUB_ACTIVE_OBJECT);
		this.topId = topId;
		this.topChildId = topChildId;
		this.subId = subId;
		this.overlay = overlay;
		this.object = object;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		
		stream.writeIntV2(topUid);
        stream.writeIntV2(xteas[2]);
        stream.writeByte128(overlay ? 1 : 0);
        stream.writeInt(xteas[1]);
        stream.writeIntV1(xteas[0]);
        stream.writeShort128(subId);
        stream.write128Byte((object.getType().id << 2) | (object.getRotation() & 0x3));
        stream.writeInt(xteas[3]);
        stream.writeIntV2((object.getPlane() << 28) | (object.getX() << 14) | object.getY());
        stream.writeInt(object.getId());
	}

}
