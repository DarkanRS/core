package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetAngle extends PacketEncoder {
	
	private int interfaceHash;
	private int pitch;
	private int roll;
	private int scale;

	public IFSetAngle(int interfaceId, int componentId, int pitch, int roll, int scale) {
		super(ServerPacket.IF_SETANGLE);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.pitch = pitch;
		this.roll = roll;
		this.scale = scale;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(interfaceHash);
		stream.writeShortLE(pitch);
		stream.writeShortLE(roll);
		stream.writeShortLE128(scale);
	}

}
