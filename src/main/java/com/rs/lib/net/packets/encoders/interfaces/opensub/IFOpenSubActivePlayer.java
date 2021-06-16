package com.rs.lib.net.packets.encoders.interfaces.opensub;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenSubActivePlayer extends PacketEncoder {

	private int pid;
	private boolean overlay;
	private int topId;
	private int topChildId;
	private int subId;
	
	public IFOpenSubActivePlayer(int topId, int topChildId, int subId, boolean overlay, int pid) {
		super(ServerPacket.IF_OPENSUB_ACTIVE_PLAYER);
		this.pid = pid;
		this.topId = topId;
		this.topChildId = topChildId;
		this.subId = subId;
		this.overlay = overlay;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		int topUid = topId << 16 | topChildId;
		
		stream.writeShort128(pid);
        stream.writeIntLE(xteas[1]);
        stream.writeByteC(overlay ? 1 : 0);
        stream.writeInt(topUid);
        stream.writeIntLE(xteas[3]);
        stream.writeShortLE128(subId);
        stream.writeInt(xteas[2]);
        stream.writeInt(xteas[0]);
	}

}
