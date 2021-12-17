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

@PacketDecoder({ ClientPacket.PLAYER_OP1, ClientPacket.PLAYER_OP2, ClientPacket.PLAYER_OP3, ClientPacket.PLAYER_OP4, ClientPacket.PLAYER_OP5, ClientPacket.PLAYER_OP6, ClientPacket.PLAYER_OP7, ClientPacket.PLAYER_OP8, ClientPacket.PLAYER_OP9, ClientPacket.PLAYER_OP10 })
public class PlayerOp extends Packet {
	
	private int pid;
	private boolean forceRun;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		PlayerOp p = new PlayerOp();
		p.pid = stream.readShort();
		p.forceRun = stream.read128Byte() == 1;
		return p;
	}

	public int getPid() {
		return pid;
	}

	public boolean isForceRun() {
		return forceRun;
	}

}
