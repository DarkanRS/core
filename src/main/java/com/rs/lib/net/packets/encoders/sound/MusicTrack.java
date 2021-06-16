package com.rs.lib.net.packets.encoders.sound;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MusicTrack extends PacketEncoder {
	
	private int id, delay, volume;

	public MusicTrack(int id, int delay, int volume) {
		super(ServerPacket.MUSIC_TRACK);
		this.id = id;
		this.delay = delay;
		this.volume = volume;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte128(delay);
		stream.writeByte128(volume);
		stream.writeShort128(id);
	}

}
