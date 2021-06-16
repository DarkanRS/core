package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class DrawOrder extends PacketEncoder {
	
	private boolean playerFirst;

	public DrawOrder(boolean playerFirst) {
		super(ServerPacket.SET_DRAW_ORDER);
		this.playerFirst = playerFirst;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByteC(playerFirst ? 1 : 0);
	}

}
