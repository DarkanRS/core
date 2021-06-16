package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetAnimation extends PacketEncoder {
	
	private int interfaceHash;
	private int animationId;
	
	public IFSetAnimation(int interfaceId, int componentId, int animationId) {
		super(ServerPacket.IF_SETANIM);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.animationId = animationId;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntV2(animationId);
		stream.writeInt(interfaceHash);
	}

}
