package com.rs.lib.net.packets.encoders.sound;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class SoundVorbis extends PacketEncoder {
	
	private int id, delay;

	public SoundVorbis(int id, int delay) {
		super(ServerPacket.VORBIS_SOUND);
		this.id = id;
		this.delay = delay;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(id);
		stream.writeByte(1);
		stream.writeShort(delay);
		stream.writeByte(255);
		stream.writeShort(256);
	}

}
