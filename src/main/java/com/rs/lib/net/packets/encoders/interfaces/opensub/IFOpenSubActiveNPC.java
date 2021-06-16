package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSubActiveNPC extends PacketEncoder {

	private int npcSid;
	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;
	
	public IFOpenSubActiveNPC(int topId, int topChildId, int subId, boolean overlay, int npcSid) {
		super(ServerPacket.IF_OPENSUB_ACTIVE_NPC);
		this.npcSid = npcSid;
		this.topId = topId;
		this.topChildId = topChildId;
		this.subId = subId;
		this.overlay = overlay;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		
		stream.writeIntLE(xteas[3]);
        stream.writeByte(overlay ? 1 : 0);
        stream.writeIntV1(xteas[0]);
        stream.writeShort(subId);
        stream.writeInt(xteas[1]);
        stream.writeIntV1(topUid);
        stream.writeIntV1(xteas[2]);
        stream.writeShortLE(npcSid);
	}

}
