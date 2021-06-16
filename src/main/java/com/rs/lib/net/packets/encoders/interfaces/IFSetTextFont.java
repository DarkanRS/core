package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetTextFont extends PacketEncoder {
	
	private int interfaceHash;
	private int fontId;

	public IFSetTextFont(int interfaceId, int componentId, int fontId) {
		super(ServerPacket.IF_SETTEXTFONT);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.fontId = fontId;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(fontId);
		stream.writeIntV1(interfaceHash);
	}

}
