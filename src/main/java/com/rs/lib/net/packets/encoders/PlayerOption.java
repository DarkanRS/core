package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class PlayerOption extends PacketEncoder {
	
	private String option;
	private int slot;
	private boolean top;
	private int cursor;

	public PlayerOption(String option, int slot, boolean top, int cursor) {
		super(ServerPacket.PLAYER_OPTION);
		this.option = option;
		this.slot = slot;
		this.top = top;
		this.cursor = cursor;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(option);
		stream.writeByte128(slot);
		stream.writeShortLE128(cursor);
		stream.writeByteC(top ? 1 : 0);
	}

}
