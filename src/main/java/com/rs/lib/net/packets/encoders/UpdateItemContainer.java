// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
