package com.rs.cache.loaders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.loaders.cs2.CS2ParamDefs;
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.lib.Constants;
import com.rs.lib.game.WorldTile;
import com.rs.lib.io.InputStream;

public final class StructDefinitions {

	private HashMap<Long, Object> values;

	private static final ConcurrentHashMap<Integer, StructDefinitions> maps = new ConcurrentHashMap<Integer, StructDefinitions>();
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		File file = new File("structs.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append("//Version = 727\n");
		writer.flush();
		for (int i = 0; i < 100000; i++) {
			StructDefinitions map = getStruct(i);
			if (map == null || map.getValues() == null)
				continue;
			writer.append(i + " - " + map);
			writer.newLine();
			writer.flush();
			System.out.println(i + " - " + map.getValues().toString());
		}
		writer.close();
	}

	public static final StructDefinitions getStruct(int structId) {
		StructDefinitions script = maps.get(structId);
		if (script != null)
			return script;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.STRUCTS.getId(), structId);
		script = new StructDefinitions();
		if (data != null)
			script.readValueLoop(new InputStream(data));
		maps.put(structId, script);
		return script;
	}

	public HashMap<Long, Object> getValues() {
		return values;
	}

	public Object getValue(long key) {
		if (values == null)
			return null;
		return values.get(key);
	}

	public long getKeyForValue(Object value) {
		for (Long key : values.keySet()) {
			if (values.get(key).equals(value))
				return key;
		}
		return -1;
	}

	public int getSize() {
		if (values == null)
			return 0;
		return values.size();
	}

	public int getIntValue(long key) {
		if (values == null)
			return 0;
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer))
			return 0;
		return (Integer) value;
	}
	
	public int getIntValue(long key, int defaultVal) {
		if (values == null)
			return defaultVal;
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer))
			return defaultVal;
		return (Integer) value;
	}

	public String getStringValue(long key) {
		if (values == null)
			return "";
		Object value = values.get(key);
		if (value == null || !(value instanceof String))
			return "";
		return (String) value;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (values == null)
				values = new HashMap<Long, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				values.put((long) key, value);
			}
		}
	}
	
	public Object valToType(long id, Object o) {
		if (CS2ParamDefs.getParams((int) id).type == CS2Type.ICOMPONENT) {
			if (o instanceof String)
				return o;
			long interfaceId = ((int) o) >> 16;
			long componentId = ((int) o) - (interfaceId << 16);
			return "IComponent("+interfaceId+", "+componentId+")";
		} else if (CS2ParamDefs.getParams((int) id).type  == CS2Type.LOCATION) {
			if (o instanceof String)
				return o;
			return new WorldTile(((int) o));
		} else if (CS2ParamDefs.getParams((int) id).type  == CS2Type.SKILL) {
			if (o instanceof String)
				return o;
			int idx = (int) o;
			if (idx >= Constants.SKILL_NAME.length)
				return o;
			return idx+"("+Constants.SKILL_NAME[((int) o)]+")";
		} else if (CS2ParamDefs.getParams((int) id).type  == CS2Type.ITEM) {
			if (o instanceof String)
				return o;
			return ((int) o)+" ("+ItemDefinitions.getDefs(((int) o)).getName()+")"; 
		} else if (CS2ParamDefs.getParams((int) id).type  == CS2Type.NPCDEF) {
			if (o instanceof String)
				return o;
			return ((int) o)+" ("+NPCDefinitions.getDefs(((int) o)).getName()+")"; 
		} else if (CS2ParamDefs.getParams((int) id).type  == CS2Type.STRUCT) {
			return o + ": " + StructDefinitions.getStruct((int) o).getValues(); 
		}
		return o;
	}
	
	@Override
	public String toString() {
		if (getValues() == null)
			return "null";
		StringBuilder s = new StringBuilder();
		s.append("{");
		for (Long l : getValues().keySet()) {
			s.append("\n\t");
			s.append(l + " ("+CS2ParamDefs.getParams(l.intValue()).type+")");
			s.append(" = ");
			s.append(valToType(l, getValues().get(l)) + ", ");
		}
		s.append("\n}");
		return s.toString();
	}

	private StructDefinitions() {
	}
}
