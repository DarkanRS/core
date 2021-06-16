package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFOpenTop extends PacketEncoder {
	
	private int interfaceId;
	private int type;

	public IFOpenTop(int interfaceId, int type) {
		super(ServerPacket.IF_OPENTOP);
		this.interfaceId = interfaceId;
		this.type = type;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int[] xteas = new int[4];
		stream.writeIntV1(xteas[2]);
		stream.writeIntV1(xteas[3]);
		stream.writeShort128(interfaceId);
		stream.write128Byte(type);
		stream.writeIntLE(xteas[1]);
		stream.writeIntLE(xteas[0]);
	}

}
