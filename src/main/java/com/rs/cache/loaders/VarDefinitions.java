package com.rs.cache.loaders;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public final class VarDefinitions {

	private static final ConcurrentHashMap<Integer, VarDefinitions> VAR_DEFS = new ConcurrentHashMap<Integer, VarDefinitions>();

	public int id;
	public char paramType;
	public int defaultValue = 0;

	public static final void main(String[] args) throws IOException {
		//Cache.init();
		System.out.println("Num vars: " + Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.VARS.getId()));
		for (int i = 0; i < Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.VARS.getId())+1; i++) {
			VarDefinitions cd = getDefs(i);
			System.out.println(i + " - " + cd.paramType + " " + cd.defaultValue);
		}

	}

	public static final VarDefinitions getDefs(int id) {
		VarDefinitions script = VAR_DEFS.get(id);
		if (script != null)
			return script;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.VARS.getId(), id);
		script = new VarDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		VAR_DEFS.put(id, script);
		return script;
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
		if (opcode == 1) {
			paramType = Utils.cp1252ToChar((byte) stream.readByte());
		} else if (opcode == 5) {
			defaultValue = stream.readUnsignedShort();
		}
	}

	private VarDefinitions() {

	}
}
