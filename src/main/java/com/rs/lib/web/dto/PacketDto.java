package com.rs.lib.web.dto;

import com.rs.lib.net.packets.Packet;

public class PacketDto {
	
	private String username;
	private Packet[] packets;
	
	public PacketDto(String username, Packet... packets) {
		this.username = username;
		this.packets = packets;
	}

	public String getUsername() {
		return username;
	}

	public Packet[] getPackets() {
		return packets;
	}

}
