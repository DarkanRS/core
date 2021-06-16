package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.GroundItem;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class RemoveGroundItem extends PacketEncoder {
	
	private GroundItem item;

	public RemoveGroundItem(GroundItem item) {
		super(ServerPacket.REMOVE_GROUND_ITEM);
		this.item = item;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByteC((item.getTile().getXInChunk() << 4) | item.getTile().getYInChunk());
		stream.writeShortLE128(item.getId());
	}

}
