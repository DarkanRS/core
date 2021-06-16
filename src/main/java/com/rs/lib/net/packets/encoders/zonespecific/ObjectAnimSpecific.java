package com.rs.lib.net.packets.encoders.zonespecific;

import com.rs.lib.game.Animation;
import com.rs.lib.game.WorldObject;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ObjectAnimSpecific extends PacketEncoder {
	
	private WorldObject object;
	private Animation animation;

	public ObjectAnimSpecific(WorldObject object, Animation animation) {
		super(ServerPacket.OBJ_ANIM_SPECIFIC);
		this.object = object;
		this.animation = animation;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntV2(object.getTileHash());
		stream.writeIntLE(animation.getIds()[0]);
		stream.write128Byte((object.getType().id << 2) + (object.getRotation() & 0x3));
	}

}
