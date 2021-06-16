package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetClickMask extends PacketEncoder {
	
	private int interfaceHash;
	private boolean clickMask;
	
	public IFSetClickMask(int interfaceId, int componentId, boolean clickMask) {
		super(ServerPacket.IF_SETCLICKMASK);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.clickMask = clickMask;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		stream.write128Byte(clickMask ? 1 : 0);
		stream.writeIntLE(interfaceHash);
	}

}
