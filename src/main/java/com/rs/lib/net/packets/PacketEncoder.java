package com.rs.lib.net.packets;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.Session;

public abstract class PacketEncoder {
	
	private ServerPacket packet;
	
	public PacketEncoder(ServerPacket packet) {
		this.packet = packet;
	}
	
	public abstract void encodeBody(OutputStream stream);
	
	public final void writeToStream(OutputStream stream, Session session) {
		switch(packet.size) {
		case -1:
			stream.writePacketVarByte(session == null ? null : session.getIsaac(), packet.opcode, true);
			encodeBody(stream);
			stream.endPacketVarByte();
			break;
		case -2:
			stream.writePacketVarShort(session == null ? null : session.getIsaac(), packet.opcode, true);
			encodeBody(stream);
			stream.endPacketVarShort();
			break;
		default:
			stream.writePacket(session == null ? null : session.getIsaac(), packet.opcode, true);
			encodeBody(stream);
			break;
		}
	}
	
	public ServerPacket getPacket() {
		return packet;
	}
	
}
