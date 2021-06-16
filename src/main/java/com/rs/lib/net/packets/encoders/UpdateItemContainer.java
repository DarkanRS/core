package com.rs.lib.net.packets.encoders;

import com.rs.lib.game.Item;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateItemContainer extends PacketEncoder {
	
	private int key;
	private boolean negativeKey;
	private Item[] items;
	private int[] slots;

	public UpdateItemContainer(int key, boolean negativeKey, Item[] items, int... slots) {
		super(slots != null && slots.length > 0 ? ServerPacket.UPDATE_INV_PARTIAL : ServerPacket.UPDATE_INV_FULL);
		this.key = key;
		this.negativeKey = negativeKey;
		this.items = items;
		this.slots = slots;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		if (slots != null && slots.length > 0) {
			stream.writeShort(key);
			stream.writeByte(negativeKey ? 1 : 0);
			for (int slotId : slots) {
				if (slotId >= items.length)
					continue;
				stream.writeSmart(slotId);
				int id = -1;
				int amount = 0;
				Item item = items[slotId];
				if (item != null) {
					id = item.getId();
					amount = item.getAmount();
				}
				stream.writeShort(id + 1);
				if (id != -1) {
					stream.writeByte(amount >= 255 ? 255 : amount);
					if (amount >= 255)
						stream.writeInt(amount);
				}
			}
		} else {
			stream.writeShort(key);
			stream.writeByte(negativeKey ? 1 : 0);
			stream.writeShort(items.length);
			for (int index = 0; index < items.length; index++) {
				Item item = items[index];
				int id = -1;
				int amount = 0;
				if (item != null) {
					id = item.getId();
					amount = item.getAmount();
				}
				stream.write128Byte(amount >= 255 ? 255 : amount);
				if (amount >= 255)
					stream.writeIntV2(amount);
				stream.writeShortLE128(id + 1);
			}
		}
	}

}
