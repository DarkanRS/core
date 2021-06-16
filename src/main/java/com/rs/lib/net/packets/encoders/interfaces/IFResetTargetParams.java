package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFResetTargetParams extends PacketEncoder {
	
	private int interfaceHash;
	private int fromSlot;
	private int toSlot;

	public IFResetTargetParams(int interfaceId, int componentId, int fromSlot, int toSlot) {
		super(ServerPacket.IF_RESETTARGETPARAM);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.fromSlot = fromSlot;
		this.toSlot = toSlot;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeIntV1(interfaceHash);
		stream.writeShortLE128(interfaceHash >> 16);
		stream.writeShort(toSlot);
		stream.writeShort128(fromSlot);
	}

}
