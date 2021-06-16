package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.IF_ON_PLAYER)
public class IFOnPlayer extends Packet {
	
	private int interfaceId;
	private int componentId;
	private int slotId;
	private int itemId;
	private int playerIndex;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFOnPlayer p = new IFOnPlayer();
		p.slotId = stream.readUnsignedShort();
		p.playerIndex = stream.readUnsignedShortLE();
		@SuppressWarnings("unused")
		boolean unknown = stream.read128Byte() == 1;
		int interfaceHash = stream.readIntV2();
		p.itemId = stream.readUnsignedShortLE();
		p.interfaceId = interfaceHash >> 16;
		p.componentId = interfaceHash - (p.interfaceId << 16);
		if (p.componentId == 65535)
			p.componentId = -1;
		return p;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}

	public int getComponentId() {
		return componentId;
	}

	public int getSlotId() {
		return slotId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

}
