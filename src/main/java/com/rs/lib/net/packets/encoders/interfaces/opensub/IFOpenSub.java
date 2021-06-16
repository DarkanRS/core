package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSub extends PacketEncoder {

	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;

	public IFOpenSub(int topId, int topChildId, int subId, boolean overlay) {
		super(ServerPacket.IF_OPENSUB);
		this.subId = subId;
		this.topId = topId;
		this.topChildId = topChildId;
		this.overlay = overlay;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		stream.writeInt(xteas[0]);
		stream.writeIntV2(xteas[2]);
		stream.writeIntV2(xteas[1]);
		stream.writeShortLE128(subId);
		stream.writeIntLE(xteas[3]);
		stream.writeInt(topUid);
		stream.writeByteC(overlay ? 1 : 0);
	}

}
