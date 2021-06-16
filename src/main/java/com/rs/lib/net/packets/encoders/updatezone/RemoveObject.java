package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.WorldObject;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class RemoveObject extends PacketEncoder {
	
	private WorldObject object;

	public RemoveObject(WorldObject object) {
		super(ServerPacket.DESTROY_OBJECT);
		this.object = object;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte128((object.getType().id << 2) + (object.getRotation() & 0x3));
		stream.writeByte((object.getXInChunk() << 4) | object.getYInChunk());
	}

}
