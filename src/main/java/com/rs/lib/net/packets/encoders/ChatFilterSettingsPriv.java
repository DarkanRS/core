package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ChatFilterSettingsPriv extends PacketEncoder {
	
	private int privateStatus;

	public ChatFilterSettingsPriv(int privateStatus) {
		super(ServerPacket.CHAT_FILTER_SETTINGS_PRIVATECHAT);
		this.privateStatus = privateStatus;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(privateStatus);
	}

}
