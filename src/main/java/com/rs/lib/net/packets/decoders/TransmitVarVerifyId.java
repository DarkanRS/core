package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.TRANSMITVAR_VERIFYID)
public class TransmitVarVerifyId extends Packet {
	
	private int id;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		TransmitVarVerifyId p = new TransmitVarVerifyId();
		p.id = stream.readInt();
		return p;
	}

	public int getId() {
		return id;
	}

}
