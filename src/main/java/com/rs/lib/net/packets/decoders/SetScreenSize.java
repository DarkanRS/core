package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.SCREEN_SIZE)
public class SetScreenSize extends Packet {
	
	private int displayMode;
	private int width, height;
	private boolean switchScreenMode;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		SetScreenSize p = new SetScreenSize();
		p.displayMode = stream.readUnsignedByte();
		p.width = stream.readUnsignedShort();
		p.height = stream.readUnsignedShort();
		p.switchScreenMode = stream.readUnsignedByte() == 1;
		return p;
	}
	
	public int getDisplayMode() {
		return displayMode;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isSwitchScreenMode() {
		return switchScreenMode;
	}
}
