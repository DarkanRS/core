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
package com.rs.lib.net.packets.decoders.mouse;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.MOUSE_BUTTON_CLICK)
public class MouseClickHW extends Packet {

	private int mouseButton;
	private int time;
	private int x, y;
	private boolean hardware;
	
	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MouseClickHW p = new MouseClickHW();
		int positionHash = stream.readIntLE();
		int flags = stream.readByte128();
		p.time = stream.readShortLE();
		p.y = positionHash >> 16;
		p.x = positionHash - (p.y << 16);
		p.mouseButton = flags >> 1;
		p.hardware = (flags - (p.mouseButton << 1)) == 0;
		return p;
	}
	
	public int getMouseButton() {
		return mouseButton;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public int getTime() {
		return time;
	}

	public boolean isHardware() {
		return hardware;
	}

}
