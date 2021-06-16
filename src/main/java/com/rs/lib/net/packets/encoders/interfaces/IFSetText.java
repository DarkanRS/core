package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetText extends PacketEncoder {
	
	private int interfaceHash;
	private String text;

	public IFSetText(int interfaceId, int componentId, String text) {
		super(ServerPacket.IF_SETTEXT);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.text = text;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(text);
		stream.writeIntV1(interfaceHash);
	}

}
