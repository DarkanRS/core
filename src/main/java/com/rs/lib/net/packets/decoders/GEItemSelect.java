package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.GE_ITEM_SELECT)
public class GEItemSelect extends Packet {
	
	private int itemId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		GEItemSelect p = new GEItemSelect();
		p.itemId = stream.readShort();
		return p;
	}

	public int getItemId() {
		return itemId;
	}

}
