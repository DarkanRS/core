package com.rs.lib.net.packets.decoders.chat;

import com.rs.cache.Cache;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;
import com.rs.lib.util.Utils;

@PacketDecoder(ClientPacket.SEND_PRIVATE_MESSAGE)
public class SendPrivateMessage extends Packet {
	
	private String toDisplayName;
	private String message;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		SendPrivateMessage p = new SendPrivateMessage();
		p.toDisplayName = stream.readString();
		p.message = Utils.fixChatMessage(Cache.STORE.getHuffman().readEncryptedMessage(150, stream));
		return p;
	}

	public String getToDisplayName() {
		return toDisplayName;
	}

	public String getMessage() {
		return message;
	}

}
