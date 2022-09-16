package com.rs.lib.net.packets.encoders;

import java.util.Objects;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class Sound extends PacketEncoder {
	public enum SoundType {
		EFFECT,
		VORB_EFFECT,
		VOICE,
		JINGLE,
		MUSIC
	}
	
	private int id;
	private int repeat;
	private int delay;
	private int volume;
	private int sampleRate;
	private SoundType type;
	
	public Sound(int id, int delay, int volume, int sampleRate, SoundType type) {
		super(soundToPacket(type));
		this.repeat = 1;
		this.id = id;
		this.delay = delay;
		this.volume = volume;
		this.sampleRate = sampleRate;
		this.type = type;
	}
	
	private static ServerPacket soundToPacket(SoundType type) {
		return switch(type) {
		case EFFECT -> ServerPacket.SOUND_SYNTH;
		case VORB_EFFECT -> ServerPacket.VORBIS_SOUND;
		case JINGLE -> ServerPacket.MUSIC_EFFECT;
		case MUSIC -> ServerPacket.MUSIC_TRACK;
		case VOICE -> ServerPacket.VORBIS_SPEECH_SOUND;
		default -> throw new IllegalArgumentException("Invalid sound type provided to sound.");
		};
	}

	public Sound(int id, int delay, int volume, SoundType type) {
		this(id, delay, volume, 256, type);
	}
	
	public Sound(int id, int delay, SoundType type) {
		this(id, delay, 255, 256, type);
	}
	
	public Sound(int id, SoundType type) {
		this(id, 0, 255, 256, type);
	}
	
	public Sound volume(int volume) {
		this.volume = volume;
		return this;
	}
	
	public Sound delay(int delay) {
		this.delay = delay;
		return this;
	}
	
	public Sound sampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
		return this;
	}
	
	public Sound repeat(int repeat) {
		this.repeat = repeat;
		return this;
	}
	
	@Override
	public void encodeBody(OutputStream stream) {
		switch(getPacket()) {
		case SOUND_SYNTH -> {
			stream.writeShort(id);
			stream.writeByte(repeat);
			stream.writeShort(delay);
			stream.writeByte(volume);
			stream.writeShort(sampleRate);
		}
		case VORBIS_SOUND -> {
			stream.writeShort(id);
			stream.writeByte(repeat);
			stream.writeShort(delay);
			stream.writeByte(volume);
			stream.writeShort(sampleRate);
		}
		case MUSIC_EFFECT -> {
			stream.write24BitIntegerV2(0);
			stream.write128Byte(volume);
			stream.writeShortLE(id);
		}
		case MUSIC_TRACK -> {
			stream.writeByte128(delay);
			stream.writeByte128(volume);
			stream.writeShort128(id);
		}
		case VORBIS_SPEECH_SOUND -> {
			stream.writeShort(id);
			stream.writeByte(repeat);
			stream.writeShort(delay);
			stream.writeByte(volume);
		}
		default -> throw new IllegalArgumentException("Invalid packet type provided to sound.");
		}
	}
	
	public int getId() {
		return id;
	}

	public int getDelay() {
		return delay;
	}

	public SoundType getType() {
		return type;
	}
	
	public int getVolume() {
		return volume;
	}

	public int getSampleRate() {
		return sampleRate;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(delay, id, volume, sampleRate, repeat, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sound other = (Sound) obj;
		return delay == other.delay && id == other.id && repeat == other.repeat && type == other.type && sampleRate == other.sampleRate && volume == other.volume;
	}

	public int getRepeat() {
		return repeat;
	}
}
