package com.rs.lib.net.packets.encoders.camera;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CamForceAngle extends PacketEncoder {

	private int angleX, angleY;
	
	public CamForceAngle(int angleX, int angleY) {
		super(ServerPacket.CAM_FORCEANGLE);
		this.angleX = angleX;
		this.angleY = angleY;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort128(angleY);
		stream.writeShortLE128(angleX);
	}

}
