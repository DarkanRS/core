package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class IgnoreList extends PacketEncoder {
	
	private String[] ignores;

	public IgnoreList(String... ignores) {
		super(ServerPacket.UPDATE_IGNORE_LIST);
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(ignores.length);
		for (String username : ignores) {
			if (username != null) {
				stream.writeString(Utils.formatPlayerNameForDisplay(username));
				stream.writeString(""); //Changed display name
			}
		}
	}

}
