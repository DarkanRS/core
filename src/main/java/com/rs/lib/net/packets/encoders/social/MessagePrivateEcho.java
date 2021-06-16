package com.rs.lib.net.packets.encoders.social;

import com.rs.cache.Cache;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.Utils;

public class MessagePrivateEcho extends PacketEncoder {
	
	private String name;
	private int rights;
	private String message;

	public MessagePrivateEcho(String name, int rights, String message) {
		super(ServerPacket.MESSAGE_PRIVATE_ECHO);
		this.name = name;
		this.rights = rights;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(/*!name.equals(displayName) ? 1 : 0*/ 0);
		stream.writeString(name);
//		if (!name.equals(displayName))
//			stream.writeString(displayName);
		for (int i = 0; i < 5; i++)
			stream.writeByte(Utils.getRandomInclusive(255));
		stream.writeByte(rights);
		Cache.STORE.getHuffman().sendEncryptMessage(stream, message);
	}

}
