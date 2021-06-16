package com.rs.lib.net.packets.encoders.camera;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CamMoveTo extends PacketEncoder {
	
	private int moveLocalX, moveLocalY;
	private int moveZ;
	private int speed1;
	private int speed2;

	public CamMoveTo(int moveLocalX, int moveLocalY, int moveZ, int speed1, int speed2) {
		super(ServerPacket.CAM_MOVETO);
		this.moveLocalX = moveLocalX;
		this.moveLocalY = moveLocalY;
		this.moveZ = moveZ;
		this.speed1 = speed1;
		this.speed2 = speed2;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(moveLocalY);
		stream.writeByte(moveLocalX);
		stream.write128Byte(speed1);
		stream.writeShortLE(moveZ >> 2);
		stream.writeByte128(speed2);
	}

}
