package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.KEY_PRESS)
public class KeyPress extends Packet {

	private int keyCode;
	private int time;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		KeyPress p = new KeyPress();
		p.keyCode = stream.readByte();
		p.time = stream.readTriByte();
		return p;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public int getTime() {
		return time;
	}
	
}
