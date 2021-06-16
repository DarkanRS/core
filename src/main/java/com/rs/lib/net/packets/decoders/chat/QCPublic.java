package com.rs.lib.net.packets.decoders.chat;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.QUICKCHAT_PUBLIC)
public class QCPublic extends Packet {
	
	private int chatType;
	private int qcId;
	private byte[] messageData;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		QCPublic p = new QCPublic();
		p.chatType = stream.readByte();
		p.qcId = stream.readUnsignedShort();
		p.messageData = null;
		if (stream.getRemaining() > 0) {
			p.messageData = new byte[stream.getRemaining()];
			stream.readBytes(p.messageData);
		}
		return p;
	}

	public int getChatType() {
		return chatType;
	}

	public int getQcId() {
		return qcId;
	}

	public byte[] getMessageData() {
		return messageData;
	}

}
