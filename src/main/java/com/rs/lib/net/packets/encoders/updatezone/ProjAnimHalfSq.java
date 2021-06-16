package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.Projectile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ProjAnimHalfSq extends PacketEncoder {
	
	private Projectile proj;

	public ProjAnimHalfSq(Projectile proj) {
		super(ServerPacket.MAP_PROJANIM_HALFSQ);
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
		
		stream.writeByte((proj.getSource().getXInChunk() << 3) | proj.getSource().getYInChunk());
		stream.writeByte(flags);
		stream.writeByte(proj.getDestination().getX() - proj.getSource().getX());
		stream.writeByte(proj.getDestination().getY() - proj.getSource().getY());
		stream.writeShort(proj.getSourceId());
		stream.writeShort(proj.getLockOnId());
		stream.writeShort(proj.getSpotAnimId());
		stream.writeByte(proj.getStartHeight());
		stream.writeByte(proj.getEndHeight());
		stream.writeShort(proj.getStartTime());
		stream.writeShort(proj.getEndTime());
		stream.writeByte(proj.getAngle());
		stream.writeShort(proj.getSlope());
	}

}
