package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.GROUND_ITEM_OP1, ClientPacket.GROUND_ITEM_OP2, ClientPacket.GROUND_ITEM_OP3, ClientPacket.GROUND_ITEM_OP4, ClientPacket.GROUND_ITEM_OP5, ClientPacket.GROUND_ITEM_EXAMINE })
public class GroundItemOp extends Packet {
	
	private int objectId;
	private int x, y;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		GroundItemOp packet = new GroundItemOp();
		packet.objectId = stream.readUnsignedShortLE128();
		packet.forceRun = stream.readByteC() == 1;
		packet.y = stream.readUnsignedShort();
		packet.x = stream.readUnsignedShort128();
		return packet;
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
