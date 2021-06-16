package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class SystemUpdateTimer extends PacketEncoder {
	
	private int delay;

	public SystemUpdateTimer(int delay) {
		super(ServerPacket.UPDATE_REBOOT_TIMER);
		this.delay = delay;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(delay);
	}

}
