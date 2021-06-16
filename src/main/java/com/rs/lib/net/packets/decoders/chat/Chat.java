package com.rs.lib.net.packets.decoders.chat;

import com.rs.cache.Cache;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;
import com.rs.lib.util.Utils;

@PacketDecoder(ClientPacket.CHAT)
public class Chat extends Packet {
	
	private int color;
	private int effect;
	private String message;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		Chat chat = new Chat();
		chat.color = Utils.clampI(stream.readUnsignedByte(), 0, 12);
		chat.effect = Utils.clampI(stream.readUnsignedByte(), 0, 5);
		chat.message = Utils.fixChatMessage(Cache.STORE.getHuffman().readEncryptedMessage(200, stream));
		return chat;
	}
	
	public int getColor() {
		return color;
	}

	public int getEffect() {
		return effect;
	}

	public String getMessage() {
		return message;
	}
}
