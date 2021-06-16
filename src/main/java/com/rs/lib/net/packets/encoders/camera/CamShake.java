package com.rs.lib.net.packets.encoders.camera;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CamShake extends PacketEncoder {
	
	//slot seems to work like this:
	//0 = camX addition
	//1 = camZ addition
	//2 = camY addition
	
	private int slotId;
	private int v1, v2, v3, v4;

	public CamShake(int slotId, int v1, int v2, int v3, int v4) {
		super(ServerPacket.CAM_SHAKE);
		this.slotId = slotId;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.write128Byte(v2);
		stream.writeShort(v4);
		stream.writeByteC(slotId);
		stream.writeByteC(v1);
		stream.write128Byte(v3);
	}

}
