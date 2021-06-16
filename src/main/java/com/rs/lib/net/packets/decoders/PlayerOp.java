package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.PLAYER_OP1, ClientPacket.PLAYER_OP2, ClientPacket.PLAYER_OP3, ClientPacket.PLAYER_OP4, ClientPacket.PLAYER_OP5, ClientPacket.PLAYER_OP6, ClientPacket.PLAYER_OP7, ClientPacket.PLAYER_OP8, ClientPacket.PLAYER_OP9, ClientPacket.PLAYER_OP10 })
public class PlayerOp extends Packet {
	
	private int pid;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		PlayerOp p = new PlayerOp();
		p.pid = stream.readShort();
		p.forceRun = stream.read128Byte() == 1;
		return p;
	}

	public int getPid() {
		return pid;
	}

	public boolean isForceRun() {
		return forceRun;
	}

}
