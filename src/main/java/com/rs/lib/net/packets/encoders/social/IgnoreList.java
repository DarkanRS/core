package com.rs.lib.net.packets.encoders.social;

import java.util.Map;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IgnoreList extends PacketEncoder {
	
	private Map<String, String> ignores;

	public IgnoreList(Map<String, String> ignores) {
		super(ServerPacket.UPDATE_IGNORE_LIST);
		this.ignores = ignores;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(ignores.size());
		for (String username : ignores.keySet()) {
			if (username != null) {
				stream.writeString(username);
				stream.writeString(ignores.get(username));
			}
		}
	}

}
