package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CUTSCENE_FINISHED)
public class CutsceneFinished extends Packet {

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		return new CutsceneFinished();
	}

}
