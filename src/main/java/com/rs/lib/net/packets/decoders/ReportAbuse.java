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

@PacketDecoder(ClientPacket.REPORT_ABUSE)
public class ReportAbuse extends Packet {
	
	private String username;
	private int type;
	private boolean mute;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ReportAbuse p = new ReportAbuse();
		this.username = stream.readString();
		this.type = stream.readUnsignedByte();
		this.mute = stream.readUnsignedByte() == 1;
		stream.readString();
		return p;
	}

	public String getUsername() {
		return username;
	}

	public int getType() {
		return type;
	}

	public boolean isMute() {
		return mute;
	}

}
