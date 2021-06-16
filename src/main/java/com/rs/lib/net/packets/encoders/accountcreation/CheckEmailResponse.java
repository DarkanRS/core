package com.rs.lib.net.packets.encoders.accountcreation;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CheckEmailResponse extends PacketEncoder {
	
	private int code;

	public CheckEmailResponse(int code) {
		super(ServerPacket.CREATE_CHECK_EMAIL_REPLY);
		this.code = code;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(code);
	}

}
