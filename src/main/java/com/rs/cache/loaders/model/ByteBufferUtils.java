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
package com.rs.cache.loaders.model;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
	
	public static int getSmart(ByteBuffer buffer) {
		int i_118_ = buffer.get(buffer.position()) & 0xff;
		if (i_118_ < 128)
			return buffer.get() & 0xFF;
		return (buffer.getShort() & 0xFFFF) - 32768;
	}
	public static int getSmart1(ByteBuffer buffer) {
		int i_118_ = buffer.get(buffer.position()) & 0xff;
		if (i_118_ < 128)
			return (buffer.get() & 0xFF) - 64;
		return (buffer.getShort() & 0xFFFF) - 49152;
	}
	public static int getSmart2(ByteBuffer buffer) {
		int i_118_ = buffer.get(buffer.position()) & 0xff;
		if (i_118_ < 128)
			return (buffer.get() & 0xFF) - 1;
		return (buffer.getShort() & 0xFFFF) - 32769;
	}
	
	public static int getMedium(ByteBuffer buffer) {
		return ((buffer.get() & 0xFF) << 16) | ((buffer.get() & 0xFF) << 8) | (buffer.get() & 0xFF);
	}
}
