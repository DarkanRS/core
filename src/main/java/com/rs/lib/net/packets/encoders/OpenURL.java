package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class OpenURL extends PacketEncoder {
	
	private String url;

	public OpenURL(String url) {
		super(ServerPacket.OPEN_URL);
		this.url = url;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(0);
		stream.writeString(url);
	}

}
