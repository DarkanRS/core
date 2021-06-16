package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetModel extends PacketEncoder {
	
	private int interfaceHash;
	private int modelId;

	public IFSetModel(int interfaceId, int componentId, int modelId) {
		super(ServerPacket.IF_SETMODEL);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.modelId = modelId;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(interfaceHash);
		stream.writeIntV2(modelId);
	}

}
