package com.rs.lib.net.packets.encoders.updatezone.partial;

import com.rs.lib.net.packets.PacketEncoder;

public class UpdateZoneEvent {
	
	private UpdateZonePacket packet;
	private PacketEncoder encoder;
	
	public UpdateZoneEvent(UpdateZonePacket packet, PacketEncoder encoder) {
		this.packet = packet;
		this.encoder = encoder;
	}
	
	public UpdateZonePacket getPacket() {
		return packet;
	}
	
	public PacketEncoder getEncoder() {
		return encoder;
	}

}
