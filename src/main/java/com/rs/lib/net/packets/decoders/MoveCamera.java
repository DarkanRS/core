package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.MOVE_CAMERA)
public class MoveCamera extends Packet {
	
	private int angleX, angleY;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MoveCamera p = new MoveCamera();
		p.angleX = stream.readShortLE128();
		p.angleY = stream.readShort128();
		return p;
	}

	public int getAngleX() {
		return angleX;
	}

	public int getAngleY() {
		return angleY;
	}

}
