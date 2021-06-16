package com.rs.lib.net.packets.decoders.chat;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.QUICKCHAT_PRIVATE)
public class QCPrivate extends Packet {
	
	private String toUsername;
	private int qcId;
	private byte[] messageData;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		QCPrivate p = new QCPrivate();
		p.toUsername = stream.readString();
		p.qcId = stream.readUnsignedShort();
		if (stream.getRemaining() > 3 + p.toUsername.length()) {
			p.messageData = new byte[stream.getRemaining() - (3 + p.toUsername.length())];
			stream.readBytes(p.messageData);
		}
		return p;
	}

	public String getToUsername() {
		return toUsername;
	}

	public int getQcId() {
		return qcId;
	}

	public byte[] getMessageData() {
		return messageData;
	}
}
