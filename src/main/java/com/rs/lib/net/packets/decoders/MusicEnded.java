package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.SOUND_EFFECT_MUSIC_ENDED)
public class MusicEnded extends Packet {

	private int musicId;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MusicEnded p = new MusicEnded();
		p.musicId = stream.readInt();
		return p;
	}

	public int getMusicId() {
		return musicId;
	}

}
