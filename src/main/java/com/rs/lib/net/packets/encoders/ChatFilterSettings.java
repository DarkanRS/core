package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ChatFilterSettings extends PacketEncoder {
	
	private int tradeStatus;
	private int publicStatus;

	public ChatFilterSettings(int tradeStatus, int publicStatus) {
		super(ServerPacket.CHAT_FILTER_SETTINGS);
		this.tradeStatus = tradeStatus;
		this.publicStatus = publicStatus;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByteC(tradeStatus);
		stream.writeByte128(publicStatus);
	}

}
