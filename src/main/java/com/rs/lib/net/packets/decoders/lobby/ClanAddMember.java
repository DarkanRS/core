package com.rs.lib.net.packets.decoders.lobby;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLAN_ADDMEMBER)
public class ClanAddMember extends Packet {
	
	private String username;
	
	public ClanAddMember() {
		
	}
	
	public ClanAddMember(String username) {
		this.setOpcode(ClientPacket.CLAN_ADDMEMBER);
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
