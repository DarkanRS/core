package com.rs.lib.net.packets.encoders.updatezone;

import com.rs.lib.game.Projectile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class ProjAnim extends PacketEncoder {
	
	private Projectile proj;

	public ProjAnim(Projectile proj) {
		super(ServerPacket.MAP_PROJANIM);
		this.proj = proj;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		int flags = ((proj.getSource().getXInChunk() << 3) | proj.getSource().getYInChunk());
		if (proj.usesTerrainHeight())
			flags |= 0x80;
		stream.writeByte(flags);
		stream.writeByte(proj.getDestination().getX() - proj.getSource().getX());
		stream.writeByte(proj.getDestination().getY() - proj.getSource().getY());
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
