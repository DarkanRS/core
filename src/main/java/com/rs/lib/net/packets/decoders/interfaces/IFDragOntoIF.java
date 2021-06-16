package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.IF_DRAG_ONTO_IF)
public class IFDragOntoIF extends Packet {
	
	private int fromInter;
	private int toInter;
	private int fromComp;
	private int toComp;
	private int fromSlot;
	private int toSlot;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFDragOntoIF p = new IFDragOntoIF();
		p.toSlot = stream.readShortLE128();
		p.fromSlot = stream.readShortLE();
		stream.readShort();
		stream.readShortLE128();
		int fromInterfaceHash = stream.readIntV1();
		int toInterfaceHash = stream.readIntLE();

		p.toInter = toInterfaceHash >> 16;
		p.toComp = toInterfaceHash - (p.toInter << 16);
		p.fromInter = fromInterfaceHash >> 16;
		p.fromComp = fromInterfaceHash - (p.fromInter << 16);
		return p;
	}

	public int getFromInter() {
		return fromInter;
	}

	public int getToInter() {
		return toInter;
	}

	public int getFromComp() {
		return fromComp;
	}

	public int getToComp() {
		return toComp;
	}

	public int getFromSlot() {
		return fromSlot;
	}

	public int getToSlot() {
		return toSlot;
	}
}
