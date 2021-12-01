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
package com.rs.lib.net.packets.encoders.interfaces;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class IFSetPlayerHeadIgnoreWorn extends PacketEncoder {
	
	private int interfaceHash;
	private int identitiKit1;
	private int identitiKit2;
	private int identitiKit3;

	public IFSetPlayerHeadIgnoreWorn(int interfaceId, int componentId, int identitiKit1, int identitiKit2, int identitiKit3) {
		super(ServerPacket.IF_SETPLAYERHEAD_IGNOREWORN);
		this.interfaceHash = interfaceId << 16 | componentId;
		this.identitiKit1 = identitiKit1;
		this.identitiKit2 = identitiKit2;
		this.identitiKit3 = identitiKit3;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShortLE(identitiKit1);
		stream.writeShort128(identitiKit2);
		stream.writeShort128(identitiKit3);
		stream.writeIntV2(interfaceHash);
	}
}
