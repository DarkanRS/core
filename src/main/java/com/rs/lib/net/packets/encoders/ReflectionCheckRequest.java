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
package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.ReflectionCheck;

public class ReflectionCheckRequest extends PacketEncoder {
	
	private ReflectionCheck check;

	public ReflectionCheckRequest(ReflectionCheck check) {
		super(ServerPacket.REFLECTION_CHECK);
		this.check = check;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(1); //amount to check
		
		stream.writeInt(check.getId()); //verification

		//for size etc
		stream.writeByte(4); //type 4 = method check
		stream.writeString(check.getClassName());
		stream.writeString(check.getMethodName());

		if (check.getParams() != null && check.getParams().length > 0) {
			stream.writeByte(check.getParams().length);
			for (String param : check.getParams())
				stream.writeString(param);
		} else {
			stream.writeByte(0);
		}

		stream.writeString(check.getReturnType()); //return type
	}
}
