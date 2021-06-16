package com.rs.cache.loaders;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;

public final class VarBitDefinitions {

	private static final ConcurrentHashMap<Integer, VarBitDefinitions> varpbitDefs = new ConcurrentHashMap<Integer, VarBitDefinitions>();

	public int id;
	public int baseVar;
	public int startBit;
	public int endBit;

	public static final void main(String[] args) throws IOException {
		//Cache.init();
		//System.out.println(getDefs(8065).baseVar);
		for (int i = 0; i < Cache.STORE.getIndex(IndexType.VARBITS).getLastArchiveId() * 0x3ff; i++) {
			VarBitDefinitions cd = getDefs(i);
			if (cd.baseVar == 1966)
				System.out.println(cd.id + ": " + cd.startBit + "->" + cd.endBit + " on varp " + cd.baseVar);
		}
	}

	public static final VarBitDefinitions getDefs(int id) {
		VarBitDefinitions script = varpbitDefs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.STORE.getIndex(IndexType.VARBITS).getFile(ArchiveType.VARBITS.archiveId(id), ArchiveType.VARBITS.fileId(id));
		script = new VarBitDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		varpbitDefs.put(id, script);
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
			baseVar = stream.readUnsignedShort();
			startBit = stream.readUnsignedByte();
			endBit = stream.readUnsignedByte();
		}
	}

	private VarBitDefinitions() {

	}
}