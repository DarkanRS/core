package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.GroundItem;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class SetGroundItemAmount extends PacketEncoder {
	
	private GroundItem item;
	private int oldAmount;

	public SetGroundItemAmount(GroundItem item, int oldAmount) {
		super(ServerPacket.GROUND_ITEM_COUNT);
		this.item = item;
		this.oldAmount = oldAmount;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte((item.getTile().getXInChunk() << 4) | item.getTile().getYInChunk());
		stream.writeShort(item.getId());
		stream.writeShort(oldAmount);
		stream.writeShort(item.getAmount());
	}

}
