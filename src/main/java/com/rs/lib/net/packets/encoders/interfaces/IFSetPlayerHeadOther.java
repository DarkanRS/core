package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class IFSetPlayerHeadOther extends PacketEncoder {
	
	private int interfaceHash;
	private String displayName;
	private int pid;

	public IFSetPlayerHeadOther(int interfaceId, int componentId, String displayName, int pid) {
		super(ServerPacket.IF_SETPLAYERHEAD_OTHER);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.displayName = displayName;
		this.pid = pid;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(Utils.stringToInt(displayName));
		stream.writeShortLE128(pid);
		stream.writeIntV1(interfaceHash);
	}

}
