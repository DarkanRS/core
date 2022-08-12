package com.rs.lib.net.packets.decoders.mouse;

public class MouseTrailStep {
	public enum Type {
		SET_POSITION,
		MOVE_OFFSET
	}
	
	private Type type;
	private int frames;
	private int x, y;
	private boolean hardware;
	
	public MouseTrailStep(Type type, int frames, int x, int y, boolean hardware) {
		this.type = type;
		this.frames = frames;
		this.x = x;
		this.y = y;
		this.hardware = hardware;
	}

	public Type getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isHardware() {
		return hardware;
	}

	public int getFrames() {
		return frames;
	}
}
