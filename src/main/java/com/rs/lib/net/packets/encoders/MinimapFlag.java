package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MinimapFlag extends PacketEncoder {
	
	private int localX;
	private int localY;

	public MinimapFlag(int localX, int localY) {
		super(ServerPacket.MINIMAP_FLAG);
		this.localX = localX;
		this.localY = localY;
	}
	
	public MinimapFlag() {
		this(255, 255);
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.write128Byte(localX);
		stream.write128Byte(localY);
	}

}
