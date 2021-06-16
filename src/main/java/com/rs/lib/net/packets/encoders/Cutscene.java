package com.rs.lib.net.packets.encoders;

import com.rs.cache.loaders.cutscenes.CutsceneArea;
import com.rs.cache.loaders.cutscenes.CutsceneDefinitions;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.MapXTEAs;

public class Cutscene extends PacketEncoder {

	private int id;
	private byte[] appearanceBlock;
	
	public Cutscene(int id, byte[] appearanceBlock) {
		super(ServerPacket.CUTSCENE);
		this.id = id;
		this.appearanceBlock = appearanceBlock;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeShort(id);
		CutsceneDefinitions def = CutsceneDefinitions.getDefs(id);
		stream.writeShort(def.areas.size());
		for (CutsceneArea area : def.areas) {
			int[] xteas = MapXTEAs.getMapKeys(area.mapBase.getRegionId());
			for (int i = 0; i < 4; i++) {
				stream.writeInt(xteas != null ? xteas[i] : 0);
			}
		}
		stream.writeByte(appearanceBlock.length);
		stream.writeBytes(appearanceBlock);
	}

}
