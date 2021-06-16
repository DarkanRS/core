package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetColor extends PacketEncoder {
	
	private int interfaceHash;
	private int color;
	
	public IFSetColor(int interfaceId, int componentId, int color) {
		super(ServerPacket.IF_SETCOLOR);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.color = color;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntLE(interfaceHash);
		stream.writeShort128(color);
	}

}
