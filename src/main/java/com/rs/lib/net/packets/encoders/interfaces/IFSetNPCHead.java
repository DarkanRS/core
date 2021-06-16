package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetNPCHead extends PacketEncoder {
	
	private int interfaceHash;
	private int npcId;

	public IFSetNPCHead(int interfaceId, int componentId, int npcId) {
		super(ServerPacket.IF_SETNPCHEAD);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.npcId = npcId;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntLE(interfaceHash);
		stream.writeIntV1(npcId);
	}

}
