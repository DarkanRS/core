package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class NPCUpdate extends PacketEncoder {
	
	private byte[] data;

	public NPCUpdate(boolean largeSceneView, byte[] data) {
		super(largeSceneView ? ServerPacket.NPC_UPDATE_LARGE : ServerPacket.NPC_UPDATE);
		this.data = data;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeBytes(data);
	}

}
