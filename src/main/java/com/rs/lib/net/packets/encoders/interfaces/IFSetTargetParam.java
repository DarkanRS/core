package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.cache.loaders.interfaces.IFTargetParams;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetTargetParam extends PacketEncoder {
	
	private IFTargetParams params;

	public IFSetTargetParam(IFTargetParams params) {
		super(ServerPacket.IF_SETTARGETPARAM);
		this.params = params;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShortLE128(params.getToSlot());
		stream.writeIntV2(params.getInterfaceId() << 16 | params.getComponentId());
		stream.writeShort(params.getFromSlot());
		stream.writeIntLE(params.getSettings());
	}

}
