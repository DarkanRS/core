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
package com.rs.lib.net.packets.decoders;

import java.lang.reflect.Modifier;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.REFLECTION_CHECK)
public class ReflectionCheckResponse extends Packet {
	
	private int id;
	private boolean exists;
	private String modifiers;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ReflectionCheckResponse p = new ReflectionCheckResponse();
		p.id = stream.readInt();
		p.exists = stream.readByte() == 1;
		if (!p.exists)
			p.modifiers = Modifier.toString(stream.readInt());
		return p;
	}

	public int getId() {
		return id;
	}

	public String getModifiers() {
		return modifiers;
	}

	public boolean exists() {
		return exists;
	}

}
