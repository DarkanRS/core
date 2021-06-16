package com.rs.lib.net.packets.encoders;

import com.rs.lib.game.HintIcon;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class HintArrow extends PacketEncoder {
	
	private HintIcon icon;

	public HintArrow(HintIcon icon) {
		super(ServerPacket.HINT_ARROW);
		this.icon = icon;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0)
			stream.skip(13);
		else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(2500);
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(icon.getPlane()); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(-1); //distance to show
			}
			stream.writeInt(icon.getModelId());
		}
	}

}
