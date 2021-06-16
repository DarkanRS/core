package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.GroundItem;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CreateGroundItem extends PacketEncoder {
	
	private GroundItem item;

	public CreateGroundItem(GroundItem item) {
		super(ServerPacket.CREATE_GROUND_ITEM);
		this.item = item;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShortLE128(item.getAmount());
		stream.writeShort128(item.getId());
		stream.writeByteC((item.getTile().getXInChunk() << 4) | item.getTile().getYInChunk());
	}

}
