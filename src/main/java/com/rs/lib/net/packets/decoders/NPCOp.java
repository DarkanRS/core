package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.NPC_OP1, ClientPacket.NPC_OP2, ClientPacket.NPC_OP3, ClientPacket.NPC_OP4, ClientPacket.NPC_OP5, ClientPacket.NPC_EXAMINE })
public class NPCOp extends Packet {
	
	private int npcIndex;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		NPCOp p = new NPCOp();
		p.npcIndex = stream.readUnsignedShort();
		p.forceRun = stream.readByte() == 1;
		return p;
	}

	public int getNpcIndex() {
		return npcIndex;
	}

	public boolean isForceRun() {
		return forceRun;
	}

}
