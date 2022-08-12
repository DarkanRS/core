package com.rs.lib.net.packets.decoders.mouse;

import java.util.ArrayList;
import java.util.List;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;
import com.rs.lib.net.packets.decoders.mouse.MouseTrailStep.Type;

@PacketDecoder(ClientPacket.MOVE_MOUSE_2)
public class MouseMoveJav extends Packet {
	
	private int frameCount;
	private int frameSteps;
	private List<MouseTrailStep> steps = new ArrayList<>();

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		MouseMoveJav p = new MouseMoveJav();
		p.frameSteps = stream.readUnsignedByte();
		p.frameCount = stream.readUnsignedByte();
		while(stream.getRemaining() > 0) {
			int peek = stream.peekUnsignedByte();
			if (peek >= 224) {
				int frames = stream.readUnsignedShort() - 57344;
				int posHash = stream.readInt();
				int dY = posHash >> 16;
				int dX = posHash & 0xFFFF;
				p.steps.add(new MouseTrailStep(Type.SET_POSITION, frames, dX, dY, true));
			} else if (peek >= 192) {
				int frames = stream.readUnsignedByte() - 192;
				int posHash = stream.readInt();
				int dY = posHash >> 16;
				int dX = posHash & 0xFFFF;
				p.steps.add(new MouseTrailStep(Type.SET_POSITION, frames, dX, dY, true));
			} else if (peek >= 128) {
				int frames = stream.readUnsignedByte() - 128;
				int posHash = stream.readUnsignedShort();
				int dY = posHash >> 8;
				int dX = posHash & 0xFF;
				p.steps.add(new MouseTrailStep(Type.MOVE_OFFSET, frames, dX, dY, true));
			} else {
				int hash = stream.readUnsignedShort();
				int frames = hash >> 12;
				int dX = hash >> 6 & 0x3F;
				int dY = hash & 0x3F;
				p.steps.add(new MouseTrailStep(Type.MOVE_OFFSET, frames, dX, dY, true));
			}
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
