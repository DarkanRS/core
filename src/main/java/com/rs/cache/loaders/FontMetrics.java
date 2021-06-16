package com.rs.cache.loaders;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class FontMetrics {
	
	private static final HashMap<Integer, FontMetrics> CACHE = new HashMap<>();
	
	private static int p11Full, p12Full, b12Full;
	
	public int id;
	byte[] characters;
	public int anInt4975;
	public int anInt4978;
	public int anInt4979;
	byte[][] kerning;
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		
		p11Full = Cache.STORE.getIndex(IndexType.NORMAL_FONTS).getArchiveId("p11_full");
		p12Full = Cache.STORE.getIndex(IndexType.NORMAL_FONTS).getArchiveId("p12_full");
		b12Full = Cache.STORE.getIndex(IndexType.NORMAL_FONTS).getArchiveId("b12_full");
		
		System.out.println(p11Full);
		System.out.println(p12Full);
		System.out.println(b12Full);
		System.out.println(get(p11Full));
		System.out.println(get(p12Full));
		System.out.println(get(b12Full));
		
//		for (int i = 0;i < Cache.STORE.getIndex(IndexType.FONT_METRICS).getLastArchiveId()+1;i++) {
//			FontMetrics defs = get(i);
//			System.out.println(defs);
//		}
	}
	
	public static final FontMetrics get(int id) {
		FontMetrics defs = CACHE.get(id);
		if (defs != null)
			return defs;
		
		byte[] data = Cache.STORE.getIndex(IndexType.FONT_METRICS).getFile(id);
		
		defs = new FontMetrics();
		defs.id = id;
		if (data != null)
			defs.decode(new InputStream(data));
		CACHE.put(id, defs);
		return defs;
	}
	
	private void decode(InputStream stream) {
		int i_3 = stream.readUnsignedByte();
		if (i_3 != 0) {
			throw new RuntimeException("");
		} else {
			boolean bool_4 = stream.readUnsignedByte() == 1;
			this.characters = new byte[256];
			stream.readBytes(this.characters, 0, 256);
			if (bool_4) {
				int[] ints_5 = new int[256];
				int[] ints_6 = new int[256];

				int i_7;
				for (i_7 = 0; i_7 < 256; i_7++) {
					ints_5[i_7] = stream.readUnsignedByte();
				}

				for (i_7 = 0; i_7 < 256; i_7++) {
					ints_6[i_7] = stream.readUnsignedByte();
				}

				byte[][] bytes_12 = new byte[256][];

				int i_10;
				for (int i_8 = 0; i_8 < 256; i_8++) {
					bytes_12[i_8] = new byte[ints_5[i_8]];
					byte b_9 = 0;

					for (i_10 = 0; i_10 < bytes_12[i_8].length; i_10++) {
						b_9 += stream.readByte();
						bytes_12[i_8][i_10] = b_9;
					}
				}

				byte[][] bytes_13 = new byte[256][];

				int i_14;
				for (i_14 = 0; i_14 < 256; i_14++) {
					bytes_13[i_14] = new byte[ints_5[i_14]];
					byte b_15 = 0;

					for (int i_11 = 0; i_11 < bytes_13[i_14].length; i_11++) {
						b_15 += stream.readByte();
						bytes_13[i_14][i_11] = b_15;
					}
				}

				this.kerning = new byte[256][256];

				for (i_14 = 0; i_14 < 256; i_14++) {
					if (i_14 != 32 && i_14 != 160) {
						for (i_10 = 0; i_10 < 256; i_10++) {
							if (i_10 != 32 && i_10 != 160) {
								this.kerning[i_14][i_10] = (byte) Utils.calculateKerning(bytes_12, bytes_13, ints_6, this.characters, ints_5, i_14, i_10);
							}
						}
					}
				}

				this.anInt4975 = ints_6[32] + ints_5[32];
			} else {
				this.anInt4975 = stream.readUnsignedByte();
			}

			stream.readUnsignedByte();
			stream.readUnsignedByte();
			this.anInt4978 = stream.readUnsignedByte();
			this.anInt4979 = stream.readUnsignedByte();
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			result.append("  ");
			try {
				result.append(field.getType().getCanonicalName() + " " + field.getName() + ": ");
				result.append(Utils.getFieldValue(this, field));
			} catch (Throwable ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
	
}
