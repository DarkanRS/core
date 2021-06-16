package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.Animation;
import com.rs.lib.game.WorldObject;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ObjectAnim extends PacketEncoder {
	
	private WorldObject object;
	private Animation animation;

	public ObjectAnim(WorldObject object, Animation animation) {
		super(ServerPacket.OBJ_ANIM);
		this.object = object;
		this.animation = animation;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.write128Byte((object.getType().id << 2) + (object.getRotation() & 0x3));
		stream.write128Byte((object.getXInChunk() << 4) | object.getYInChunk());
		stream.writeIntLE(animation.getIds()[0]);
	}

}
