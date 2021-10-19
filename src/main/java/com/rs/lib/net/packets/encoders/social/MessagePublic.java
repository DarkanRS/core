package com.rs.lib.net.packets.encoders.social;

import com.rs.cache.Cache;
import com.rs.lib.game.PublicChatMessage;
import com.rs.lib.game.QuickChatMessage;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MessagePublic extends PacketEncoder {
	
	private int pid;
	private int messageIcon;
	private PublicChatMessage message;

	public MessagePublic(int pid, int messageIcon, PublicChatMessage message) {
		super(ServerPacket.MESSAGE_PUBLIC);
		this.pid = pid;
		this.messageIcon = messageIcon;
		this.message = message;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(pid);
		stream.writeShort(message.getEffects());
		stream.writeByte(messageIcon);
		String filtered = message.getMessage();
		if (message instanceof QuickChatMessage qc) {
			stream.writeShort(qc.getFileId());
			if (qc.getData() != null)
				stream.writeBytes(qc.getData());
		} else {
			byte[] chatStr = new byte[250];
			chatStr[0] = (byte) filtered.length();
			int offset = 1 + Cache.STORE.getHuffman().encryptMessage(1, filtered.length(), chatStr, 0, filtered.getBytes());
			stream.writeBytes(chatStr, 0, offset);
		}
	}

}
