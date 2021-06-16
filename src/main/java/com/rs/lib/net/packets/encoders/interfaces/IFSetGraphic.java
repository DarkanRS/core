package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetGraphic extends PacketEncoder {
	
	private int interfaceHash;
	private int spriteId;
	
	public IFSetGraphic(int interfaceId, int componentId, int spriteId) {
		super(ServerPacket.IF_SETGRAPHIC);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.spriteId = spriteId;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntV2(spriteId);
		stream.writeIntV2(interfaceHash);
	}

}
