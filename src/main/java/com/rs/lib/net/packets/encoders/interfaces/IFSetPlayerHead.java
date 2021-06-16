package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetPlayerHead extends PacketEncoder {
	
	private int interfaceHash;
	
	public IFSetPlayerHead(int interfaceId, int componentId) {
		super(ServerPacket.IF_SETPLAYERHEAD);
		this.interfaceHash = interfaceId << 16 | componentId;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntLE(interfaceHash);
	}

}
