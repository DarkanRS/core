package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.WorldObject;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class AddObject extends PacketEncoder {
	
	private WorldObject object;

	public AddObject(WorldObject object) {
		super(ServerPacket.CREATE_OBJECT);
		this.object = object;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(object.getId());
		stream.write128Byte((object.getXInChunk() << 4) | object.getYInChunk());
		stream.write128Byte((object.getType().id << 2) + (object.getRotation() & 0x3));
	}

}
