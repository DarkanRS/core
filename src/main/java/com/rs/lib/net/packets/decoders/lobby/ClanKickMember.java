package com.rs.lib.net.packets.decoders.lobby;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLAN_KICKMEMBER)
public class ClanKickMember extends Packet {
	
	private String username;
	
	public ClanKickMember() {
		
	}
	
	public ClanKickMember(String username) {
		this.setOpcode(ClientPacket.CLAN_KICKMEMBER);
		this.username = username;
	}

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		return null;
	}

	public String getUsername() {
		return username;
	}

}
