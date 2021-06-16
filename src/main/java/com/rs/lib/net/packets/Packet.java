package com.rs.lib.net.packets;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;

public abstract class Packet {
	private ClientPacket opcode;
	
	public abstract Packet decodeAndCreateInstance(InputStream stream);
	
	public final ClientPacket getOpcode() {
		return opcode;
	}
	
	public final Packet setOpcode(ClientPacket opcode) {
		this.opcode = opcode;
		return this;
	}
}
