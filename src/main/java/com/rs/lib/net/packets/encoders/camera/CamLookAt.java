package com.rs.lib.net.packets.encoders.camera;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CamLookAt extends PacketEncoder {
	
	private int viewLocalX, viewLocalY, viewZ;
	private int speed1, speed2;

	public CamLookAt(int viewLocalX, int viewLocalY, int viewZ, int speed1, int speed2) {
		super(ServerPacket.CAM_LOOKAT);
		this.viewLocalX = viewLocalX;
		this.viewLocalY = viewLocalY;
		this.viewZ = viewZ;
		this.speed1 = speed1;
		this.speed2 = speed2;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(viewLocalX);
		stream.writeShort128(viewZ >> 2);
		stream.writeByte128(viewLocalY);
		stream.writeByteC(speed1);
		stream.write128Byte(speed2);
	}

}
