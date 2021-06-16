package com.rs.lib.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class MapXTEAs {

	private final static HashMap<Integer, int[]> XTEA_KEYS = new HashMap<Integer, int[]>();
	private final static String PACKED_PATH = "data/map/archiveKeys/packed.mcx";

	public static final int[] getMapKeys(int regionId) {
		if (XTEA_KEYS.isEmpty())
			loadKeys();
		int[] arr = XTEA_KEYS.get(regionId);
		if (arr == null || (arr[0] == 0 && arr[1] == 0 && arr[2] == 0 && arr[3] == 0))
			return null;
		return arr;
	}

	public static void loadKeys() {
		if (new File(PACKED_PATH).exists())
			loadPackedKeys();
		else
			loadUnpackedKeys();
	}

	private static final void loadPackedKeys() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int regionId = buffer.getShort() & 0xffff;
				int[] xteas = new int[4];
				for (int index = 0; index < 4; index++)
					xteas[index] = buffer.getInt();
				XTEA_KEYS.put(regionId, xteas);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadUnpackedKeys() {
		Logger.log("MapArchiveKeys", "Packing map containers xteas...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			File unpacked = new File("data/map/archiveKeys/unpacked/");
			File[] xteasFiles = unpacked.listFiles();
			for (File region : xteasFiles) {
				String name = region.getName();
				if (!name.contains(".txt")) {
					region.delete();
					continue;
				}
				int regionId = Short.parseShort(name.replace(".txt", ""));
				if (regionId <= 0) {
					region.delete();
					continue;
				}
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] xteas = new int[4];
				for (int index = 0; index < 4; index++) {
					xteas[index] = Integer.parseInt(in.readLine());
					out.writeInt(xteas[index]);
				}
				XTEA_KEYS.put(regionId, xteas);
				in.close();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MapXTEAs() {

	}

}
