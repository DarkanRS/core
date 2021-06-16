package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFCloseSub extends PacketEncoder {

	private int componentId;
	
	public IFCloseSub(int componentId) {
		super(ServerPacket.IF_CLOSESUB);
		this.componentId = componentId;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(componentId);
	}

}
