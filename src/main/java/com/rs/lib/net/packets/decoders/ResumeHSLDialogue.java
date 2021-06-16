package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.RESUME_HSLDIALOG)
public class ResumeHSLDialogue extends Packet {
	
	private int colorId;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ResumeHSLDialogue d = new ResumeHSLDialogue();
		d.colorId = stream.readUnsignedShort();
		return d;
	}

	public int getColorId() {
		return colorId;
	}

}
