package com.rs.lib.net.packets.decoders.mouse;

import java.util.ArrayList;
import java.util.List;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;
import com.rs.lib.net.packets.decoders.mouse.MouseTrailStep.Type;

@PacketDecoder(ClientPacket.MOVE_MOUSE)
public class MouseMoveHW extends Packet {
	private int frameCount;
	private int frameSteps;
	private List<MouseTrailStep> steps = new ArrayList<>();

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MouseMoveHW p = new MouseMoveHW();
		p.frameSteps = stream.readUnsignedByte();
		p.frameCount = stream.readUnsignedByte();
		while(stream.getRemaining() > 0) {
			int peek = stream.peekUnsignedByte();
			int frames, dX, dY;
			Type type = Type.MOVE_OFFSET;
			if (peek >= 224) {
				frames = stream.readUnsignedShort() - 57344;
				int posHash = stream.readInt();
				dY = posHash >> 16;
				dX = posHash & 0xFFFF;
				type = Type.SET_POSITION;
			} else if (peek >= 192) {
				frames = stream.readUnsignedByte() - 192;
				int posHash = stream.readInt();
				dY = posHash >> 16;
				dX = posHash & 0xFFFF;
				type = Type.SET_POSITION;
			} else if (peek >= 128) {
				frames = stream.readUnsignedByte() - 128;
				int posHash = stream.readUnsignedShort();
				dY = posHash >> 8;
				dX = posHash & 0xFF;
			} else {
				int hash = stream.readUnsignedShort();
				frames = hash >> 12;
				dX = hash >> 6 & 0x3F;
				dY = hash & 0x3F;
			}
			steps.add(new MouseTrailStep(type, frames, dX, dY, stream.readByte() == 0));
		}
		return p;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public int getFrameSteps() {
		return frameSteps;
	}
	
	public List<MouseTrailStep> getSteps() {
		return steps;
	}
}
