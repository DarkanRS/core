package com.rs.cache.utils;

import java.math.BigInteger;
import java.util.zip.CRC32;

public final class CacheUtil {

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}
	
	public static int getCrcChecksum(byte[] buffer, int length) {
		CRC32 crc = new CRC32();
		crc.update(buffer, 0, length);
		return (int) crc.getValue();
	}
	
	public static int getNameHash(String name) {
		return name.toLowerCase().hashCode();
	}
	
	private CacheUtil() {

	}

}
