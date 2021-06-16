package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.IF_OP1, ClientPacket.IF_OP2, ClientPacket.IF_OP3, ClientPacket.IF_OP4, ClientPacket.IF_OP5, 
	ClientPacket.IF_OP6, ClientPacket.IF_OP7, ClientPacket.IF_OP8, ClientPacket.IF_OP9, ClientPacket.IF_OP10 })
public class IFOp extends Packet {
	
	private int interfaceId;
	private int componentId;
	private int slotId;
	private int itemId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFOp packet = new IFOp();
		int interfaceHash = stream.readIntLE();
		packet.interfaceId = interfaceHash >> 16;
		packet.componentId = interfaceHash - (packet.interfaceId << 16);
		packet.itemId = stream.readUnsignedShort();
		packet.slotId = stream.readUnsignedShort128();
		return packet;
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

}
