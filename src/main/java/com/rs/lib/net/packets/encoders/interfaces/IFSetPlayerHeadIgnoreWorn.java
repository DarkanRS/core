package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetPlayerHeadIgnoreWorn extends PacketEncoder {
	
	private int interfaceHash;
	private int identitiKit1;
	private int identitiKit2;
	private int identitiKit3;

	public IFSetPlayerHeadIgnoreWorn(int interfaceId, int componentId, int identitiKit1, int identitiKit2, int identitiKit3) {
		super(ServerPacket.IF_SETPLAYERHEAD_IGNOREWORN);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.identitiKit1 = identitiKit1;
		this.identitiKit2 = identitiKit2;
		this.identitiKit3 = identitiKit3;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShortLE(identitiKit1);
		stream.writeShort128(identitiKit2);
		stream.writeShort128(identitiKit3);
		stream.writeIntV2(interfaceHash);
	}
}
