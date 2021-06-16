package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class BlockMinimapState extends PacketEncoder {
	
	private int state;

	public BlockMinimapState(int state) {
		super(ServerPacket.BLOCK_MINIMAP_STATE);
		this.state = state;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(state);
	}

}
