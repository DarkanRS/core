package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.OBJECT_OP1, ClientPacket.OBJECT_OP2, ClientPacket.OBJECT_OP3, ClientPacket.OBJECT_OP4, ClientPacket.OBJECT_OP5, ClientPacket.OBJECT_EXAMINE })
public class ObjectOp extends Packet {
	
	private int objectId;
	private int x, y;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ObjectOp p = new ObjectOp();
		p.y = stream.readUnsignedShort();
		p.x = stream.readUnsignedShort();
		p.objectId = stream.readInt();
		p.forceRun = stream.readUnsignedByte128() == 1;
		return p;
	}
	
	public int getObjectId() {
		return objectId;
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
