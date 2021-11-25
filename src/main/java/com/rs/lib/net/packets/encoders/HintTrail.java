package com.rs.lib.net.packets.encoders;

import com.rs.lib.game.WorldTile;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class HintTrail extends PacketEncoder {

	private WorldTile start;
	private int index = 0;
	private int modelId;
	private int[] stepsX, stepsY;
	private int size;
	
	public HintTrail(WorldTile start, int modelId, int[] stepsX, int[] stepsY, int size) {
		super(ServerPacket.HINT_TRAIL);
		this.start = start;
		this.modelId = modelId;
		this.stepsX = stepsX;
		this.stepsY = stepsY;
		this.size = size;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(index); //0-8 multiple trails
		if (size == -1) {
			stream.writeBigSmart(-1);
			return;
		}
				
		stream.writeBigSmart(modelId);
		stream.writeUnsignedSmart(size+1); //add 1 to steps since routefinder doesn't include first step?
		stream.writeShort(start.getX());
		stream.writeShort(start.getY());
		stream.writeByte(0); //0 offset to make sure first step is added
		stream.writeByte(0); //^
		for (int i = size - 1; i >= 0; i--) {
			stream.writeByte(stepsX[i] - start.getX());
			stream.writeByte(stepsY[i] - start.getY());
		}
	}

}
