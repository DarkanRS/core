package com.rs.lib.net.packets.decoders.lobby;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLAN_LEAVE)
public class ClanLeave extends Packet {

	public ClanLeave() {
		this.setOpcode(ClientPacket.CLAN_LEAVE);
	}
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		return null;
	}
	
}
