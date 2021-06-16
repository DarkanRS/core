package com.rs.lib.web.dto;

import com.rs.lib.net.packets.PacketEncoder;

public class PacketEncoderDto {
	
	private String username;
	private PacketEncoder[] encoders;
	
	public PacketEncoderDto(String username, PacketEncoder... encoders) {
		this.username = username;
		this.encoders = encoders;
	}

	public String getUsername() {
		return username;
	}

	public PacketEncoder[] getEncoders() {
		return encoders;
	}

}
