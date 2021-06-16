package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetScrollPos extends PacketEncoder {
	
	private int interfaceHash;
	private int pos;

	public IFSetScrollPos(int interfaceId, int componentId, int pos) {
		super(ServerPacket.IF_SETSCROLLPOS);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.pos = pos;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(pos);
		stream.writeInt(interfaceHash);
	}

}
