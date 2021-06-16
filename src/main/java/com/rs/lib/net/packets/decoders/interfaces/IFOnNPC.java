package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.IF_ON_NPC)
public class IFOnNPC extends Packet {

	private int interfaceId;
	private int componentId;
	private int slotId;
	private int itemId;
	private int npcIndex;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFOnNPC p = new IFOnNPC();
		int interfaceHash = stream.readIntV2();
		p.npcIndex = stream.readUnsignedShortLE128();
		@SuppressWarnings("unused")
		boolean unknown = stream.read128Byte() == 1;
		p.itemId = stream.readUnsignedShortLE128();
		p.slotId = stream.readUnsignedShort128();
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

	public int getNpcIndex() {
		return npcIndex;
	}
}
