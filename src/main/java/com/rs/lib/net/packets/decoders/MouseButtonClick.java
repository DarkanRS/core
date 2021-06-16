package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.MOUSE_BUTTON_CLICK)
public class MouseButtonClick extends Packet {

	private int positionHash;
	private int flags;
	private int time;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MouseButtonClick p = new MouseButtonClick();
		p.positionHash = stream.readIntLE();
		p.flags = stream.readByte128();
		p.time = stream.readShortLE();
		return p;
	}

	public int getPositionHash() {
		return positionHash;
	}

	public int getFlags() {
		return flags;
	}

	public int getTime() {
		return time;
	}

}
