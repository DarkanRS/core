package com.rs.cache.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public final class InventoryDefinitions {

	public int id;
	public int maxSize = 0;
	public int contentSize = 0;
	public int[] ids;
	public int[] amounts;

	private static final ConcurrentHashMap<Integer, InventoryDefinitions> maps = new ConcurrentHashMap<Integer, InventoryDefinitions>();

	public static void main(String... args) {
		//Cache.init();
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CONFIG).getValidFilesCount(ArchiveType.INVENTORIES.getId());i++) {
			InventoryDefinitions defs = InventoryDefinitions.getContainer(i);
			String shop = (i+1000)+" -1 false - Inventory "+i+" - ";
			if (defs.ids == null)
				continue;
			for (int j = 0;j < defs.ids.length;j++) {
				shop += "" + defs.ids[j] + " " + defs.amounts[j] + " ";
			}
			System.out.println(shop);
		}
	}
	
	public InventoryDefinitions(int id) {
		this.id = id;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.INVENTORIES.getId(), id);
		if (data != null)
			decode(new InputStream(data));
	}
	
	public static boolean contains(int[] arr, int id) {
		for (int i = 0;i < arr.length;i++) {
			if (arr[i] == id)
				return true;
		}
		return false;
	}

	public static final InventoryDefinitions getContainer(int id) {
		InventoryDefinitions def = maps.get(id);
		if (def != null)
			return def;
		def = new InventoryDefinitions(id);
		maps.put(id, def);
		return def;
	}
	
	public void write(Store store) {
		store.getIndex(IndexType.CONFIG).putFile(ArchiveType.INVENTORIES.getId(), id, encode());
	}
	
	private byte[] encode() {
		OutputStream stream = new OutputStream();
		
		if (maxSize != 0) {
			stream.writeByte(2);
			stream.writeShort(maxSize);
		}
		
		if (contentSize != 0) {
			stream.writeByte(4);
			stream.writeByte(contentSize);
			for (int i = 0;i < contentSize;i++) {
				stream.writeShort(ids[i]);
				stream.writeShort(amounts[i]);
			}
		}
		
		stream.writeByte(0);
		return stream.toByteArray();
	}

	private void decode(InputStream stream) {
		while(true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(opcode, stream);
		}
	}
	
	private void readValues(int opcode, InputStream stream) {
		if (opcode == 2) {
			maxSize = stream.readUnsignedShort();
		} else if (opcode == 4) {
			contentSize = stream.readUnsignedByte();
			ids = new int[contentSize];
			amounts = new int[contentSize];
			for (int i = 0; i < contentSize; i++) {
				ids[i] = stream.readUnsignedShort();
				amounts[i] = stream.readUnsignedShort();
			}
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
