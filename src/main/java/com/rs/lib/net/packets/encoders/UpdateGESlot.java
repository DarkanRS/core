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

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateGESlot extends PacketEncoder {
	
	private int slot;
	private int progress;
	private int item;
	private int price;
	private int amount;
	private int currAmount;
	private int totalPrice;

	public UpdateGESlot(int slot, int progress, int item, int price, int amount, int currAmount, int totalPrice) {
		super(ServerPacket.UPDATE_GE_SLOT);
		this.slot = slot;
		this.progress = progress;
		this.item = item;
		this.price = price;
		this.amount = amount;
		this.currAmount = currAmount;
		this.totalPrice = totalPrice;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(slot);
		stream.writeByte(progress);
		stream.writeShort(item);
		stream.writeInt(price);
		stream.writeInt(amount);
		stream.writeInt(currAmount);
		stream.writeInt(totalPrice);
	}

}
