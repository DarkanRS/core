package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class UpdateStat extends PacketEncoder {
	
	private int skillId, xp, level;

	public UpdateStat(int skillId, int xp, int level) {
		super(ServerPacket.UPDATE_STAT);
		this.skillId = skillId;
		this.xp = xp;
		this.level = level;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeInt(xp);
		stream.writeByte(skillId);
		stream.writeByte(level);
	}

}
