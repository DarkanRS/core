package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class PlayerUpdate extends PacketEncoder {
	
	private byte[] data;

	public PlayerUpdate(byte[] data) {
		super(ServerPacket.PLAYER_UPDATE);
		this.data = data;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeBytes(data);
	}

}
