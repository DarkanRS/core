package com.rs.lib.net.packets.decoders.interfaces;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.IF_ON_IF)
public class IFOnIF extends Packet {

	private int fromInter;
	private int toInter;
	private int fromComp;
	private int toComp;
	private int fromSlot;
	private int toSlot;
	private int fromItemId;
	private int toItemId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		IFOnIF p = new IFOnIF();
		p.toSlot = stream.readShortLE128();
		p.fromSlot = stream.readShortLE();
		p.toItemId = stream.readShortLE128();
		int hash2 = stream.readIntLE();
		int hash1 = stream.readIntV2();
		p.fromItemId = stream.readShortLE();
		p.fromInter = hash1 >> 16;
		p.toInter = hash2 >> 16;
		p.fromComp = hash1 & 0xFFFF;
		p.toComp = hash2 & 0xFFFF;
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

	public int getFromItemId() {
		return fromItemId;
	}

	public int getToItemId() {
		return toItemId;
	}

}
