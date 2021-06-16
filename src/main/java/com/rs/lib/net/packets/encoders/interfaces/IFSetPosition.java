package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetPosition extends PacketEncoder {
	
	private int interfaceHash;
	private int x;
	private int y;

	public IFSetPosition(int interfaceId, int componentId, int x, int y) {
		super(ServerPacket.IF_SETPOSITION);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.x = x;
		this.y = y;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort128(y);
		stream.writeIntV2(interfaceHash);
		stream.writeShortLE128(x);
	}

}
