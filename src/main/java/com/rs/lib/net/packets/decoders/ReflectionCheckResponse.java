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

import java.util.HashMap;
import java.util.Map;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.REFLECTION_CHECK)
public class ReflectionCheckResponse extends Packet {
	
	public enum ResponseCode {
		SUCCESS(0),
		NUMBER(1),
		STRING(2),
		OTHER(4),
		
		CHECK_EXCEPTION_CLASS_NOT_FOUND(-1),
		CHECK_EXCEPTION_SECURITY(-2), //Thrown when classloader for desired class is null
		CHECK_EXCEPTION_NULLPOINTER(-3),
		CHECK_EXCEPTION(-4),
		CHECK_THROWABLE(-5),
		
		RESP_EXCEPTION_CLASS_NOT_FOUND(-10),
		RESP_EXCEPTION_INVALID_CLASS(-11),
		RESP_EXCEPTION_STREAM_CORRUPTED(-12),
		RESP_EXCEPTION_OPTIONAL_DATA(-13),
		RESP_EXCEPTION_ILLEGAL_ACCESS(-14),
		RESP_EXCEPTION_ILLEGAL_ARGUMENT(-15),
		RESP_EXCEPTION_INVOCATION_TARGET(-16),
		RESP_EXCEPTION_SECURITY(-17),
		RESP_EXCEPTION_IO(-18),
		RESP_EXCEPTION_NULLPOINTER(-19),
		RESP_EXCEPTION(-20),
		RESP_THROWABLE(-21);
		
		private static Map<Integer, ResponseCode> MAP = new HashMap<>();
		
		static {
			for (ResponseCode r : ResponseCode.values())
				MAP.put(r.id, r);
		}
		
		public static ResponseCode forId(int id) {
			return MAP.get(id);
		}
		
		private int id;
		
		ResponseCode(int id) {
			this.id = id;
		}
	}
	
	private int id;
	private ResponseCode reponseCode;
	private InputStream data;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ReflectionCheckResponse p = new ReflectionCheckResponse();
		p.id = stream.readInt();
		p.data = stream;
		return p;
	}

	public int getId() {
		return id;
	}

	public InputStream getData() {
		return data;
	}

	public ResponseCode getCode() {
		return reponseCode;
	}
}
