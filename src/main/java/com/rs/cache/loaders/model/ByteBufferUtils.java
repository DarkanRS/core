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
