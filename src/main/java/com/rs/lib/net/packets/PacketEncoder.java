// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
