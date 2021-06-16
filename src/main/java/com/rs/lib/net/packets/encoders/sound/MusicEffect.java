package com.rs.lib.net.packets.encoders.sound;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MusicEffect extends PacketEncoder {
	
	private int id;

	public MusicEffect(int id) {
		super(ServerPacket.MUSIC_EFFECT);
		this.id = id;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.write24BitIntegerV2(0);
		stream.write128Byte(255);
		stream.writeShortLE(id);
	}

}
