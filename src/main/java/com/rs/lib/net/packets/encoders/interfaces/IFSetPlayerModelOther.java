package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class IFSetPlayerModelOther extends PacketEncoder {
	
	private int interfaceHash;
	private String displayName;
	private int pid;

	public IFSetPlayerModelOther(int interfaceId, int componentId, String displayName, int pid) {
		super(ServerPacket.IF_SETPLAYERMODEL_OTHER);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.displayName = displayName;
		this.pid = pid;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort128(pid);
		stream.writeIntV1(interfaceHash);
		stream.writeIntV1(Utils.stringToInt(displayName));
	}

}
