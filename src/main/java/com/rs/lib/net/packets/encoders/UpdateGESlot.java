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
