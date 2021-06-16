package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.WALK, ClientPacket.MINI_WALK })
public class Walk extends Packet {
	
	private int x, y;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		Walk p = new Walk();
		p.forceRun = stream.readUnsignedByte() == 1;
		p.x = stream.readUnsignedShort();
		p.y = stream.readUnsignedShortLE();
		return p;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isForceRun() {
		return forceRun;
	}

}
