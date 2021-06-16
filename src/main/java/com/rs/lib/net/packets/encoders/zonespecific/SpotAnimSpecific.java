package com.rs.lib.net.packets.encoders.zonespecific;

import com.rs.lib.game.SpotAnim;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class SpotAnimSpecific extends PacketEncoder {
	
	private SpotAnim spotAnim;
	private int targetHash;

	public SpotAnimSpecific(SpotAnim spotAnim, int targetHash) {
		super(ServerPacket.SPOT_ANIM_SPECIFIC);
		this.spotAnim = spotAnim;
		this.targetHash = targetHash;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByteC(0);
		stream.writeShort128(spotAnim.getId());
		stream.writeByteC(spotAnim.getSettings2Hash());
		stream.writeShort128(spotAnim.getSpeed());
		stream.writeIntLE(targetHash);
		stream.writeShortLE(spotAnim.getHeight());
	}

}
