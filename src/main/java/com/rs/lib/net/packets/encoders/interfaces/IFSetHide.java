package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetHide extends PacketEncoder {
	
	private int interfaceHash;
	private boolean hidden;

	public IFSetHide(int interfaceId, int componentId, boolean hidden) {
		super(ServerPacket.IF_SETHIDE);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.hidden = hidden;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte128(hidden ? 1 : 0);
		stream.writeInt(interfaceHash);
	}

}
