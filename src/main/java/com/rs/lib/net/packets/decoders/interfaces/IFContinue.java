package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.IF_CONTINUE)
public class IFContinue extends Packet {
	
	private int interfaceId;
	private int componentId;
	private int slotId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFContinue p = new IFContinue();
		int interfaceHash = stream.readIntV1();
		stream.readShortLE128();
		p.interfaceId = interfaceHash >> 16;
		p.slotId = (interfaceHash & 0xFF);
		p.componentId = interfaceHash - (p.interfaceId << 16);
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

}
