package com.rs.lib.net.packets.encoders.zonespecific;

import com.rs.lib.game.Projectile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ProjAnimSpecific extends PacketEncoder {
	
	private Projectile proj;

	public ProjAnimSpecific(Projectile proj) {
		super(ServerPacket.PROJANIM_SPECIFIC);
		this.proj = proj;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int flags = 0;
		if (proj.usesTerrainHeight())
			flags |= 0x1;
		if (proj.getBASFrameHeightAdjust() != -1) {
			flags |= 0x2;
			flags += (proj.getBASFrameHeightAdjust() << 2);
			//startHeight will not be multiplied by 4 with this flag active?
		}
		stream.writeByte128(proj.getEndHeight());
		stream.writeShortLE128(proj.getSourceId());
		stream.writeShortLE(proj.getEndTime());
		stream.writeShort(proj.getLockOnId());
		stream.writeByte128(flags);
		stream.writeShort128(proj.getSource().getY());
		stream.writeShort(proj.getSlope());
		stream.writeShortLE128(proj.getStartTime());
		stream.writeShortLE128(proj.getSpotAnimId());
		stream.writeByte128(proj.getAngle());
		stream.writeByteC(proj.getDestination().getX() - proj.getSource().getX());
		stream.writeByteC(proj.getDestination().getY() - proj.getSource().getY());
		stream.writeShort128(proj.getSource().getX());
		stream.writeByteC(proj.getStartHeight());
	}

}
