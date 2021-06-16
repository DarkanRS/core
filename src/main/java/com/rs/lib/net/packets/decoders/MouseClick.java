package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.MOUSE_CLICK)
public class MouseClick extends Packet {

	private int mouseButton;
	private int time;
	private int x, y;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MouseClick p = new MouseClick();
		int positionHash = stream.readIntLE();
		int mouseHash = stream.readShort();
		p.mouseButton = mouseHash >> 15;
		p.time = mouseHash - (p.mouseButton << 15);
		p.y = positionHash >> 16;
		p.x = positionHash - (p.y << 16);
		return p;
	}

	public int getMouseButton() {
		return mouseButton;
	}

	public int getTime() {
		return time;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
