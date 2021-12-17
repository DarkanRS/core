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
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder({ ClientPacket.NPC_OP1, ClientPacket.NPC_OP2, ClientPacket.NPC_OP3, ClientPacket.NPC_OP4, ClientPacket.NPC_OP5, ClientPacket.NPC_EXAMINE })
public class NPCOp extends Packet {
	
	private int npcIndex;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		NPCOp p = new NPCOp();
		p.npcIndex = stream.readUnsignedShort();
		p.forceRun = stream.readByte() == 1;
		return p;
	}

	public int getNpcIndex() {
		return npcIndex;
	}

	public boolean isForceRun() {
		return forceRun;
	}

}
