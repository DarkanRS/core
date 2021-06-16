package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.RESUME_TEXTDIALOG, ClientPacket.RESUME_CLANFORUMQFCDIALOG, ClientPacket.RESUME_NAMEDIALOG })
public class ResumeTextDialogue extends Packet {
	
	private String text;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ResumeTextDialogue p = new ResumeTextDialogue();
		p.text = stream.readString();
		return p;
	}

	public String getText() {
		return text;
	}

}
