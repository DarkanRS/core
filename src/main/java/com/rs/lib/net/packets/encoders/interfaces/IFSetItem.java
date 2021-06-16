package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetItem extends PacketEncoder {
	
	private int interfaceHash;
	private int itemId;
	private int amount;
	
	public IFSetItem(int interfaceId, int componentId, int itemId, int amount) {
		super(ServerPacket.IF_SETITEM);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.itemId = itemId;
		this.amount = amount;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(itemId);
		stream.writeInt(interfaceHash);
		stream.writeIntV2(amount);
	}

}
