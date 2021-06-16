package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.RESUME_COUNTDIALOG)
public class ResumeCountDialogue extends Packet {

	private int value;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ResumeCountDialogue p = new ResumeCountDialogue();
		p.value = stream.readInt();
		return p;
	}

	public int getValue() {
		return value;
	}
	
}
