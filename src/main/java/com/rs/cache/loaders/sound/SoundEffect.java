// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.cache.loaders.sound;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cache.loaders.animations.AnimationDefinitions;
import com.rs.cache.loaders.cs2.CS2Definitions;
import com.rs.cache.loaders.cs2.CS2Instruction;
import com.rs.cache.loaders.cs2.CS2Script;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public class SoundEffect {

	Instrument[] instruments = new Instrument[10];
	int loopBegin;
	int loopEnd;

	SoundEffect(InputStream buffer) {
		for (int i = 0; i < 10; i++) {
			int hasInstruments = buffer.readUnsignedByte();
			if (hasInstruments != 0) {
				buffer.setOffset(buffer.getOffset()-1);
				this.instruments[i] = new Instrument();
				this.instruments[i].decodeInstruments(buffer);
			}
		}

		this.loopBegin = buffer.readUnsignedShort();
		this.loopEnd = buffer.readUnsignedShort();
	}
	
	public static void main(String[] args) throws IOException {
		Cache.init("../cache/");
		
		Set<Integer> autoUsedIds = new HashSet<>();
		
		for (int i = 0;i < Utils.getAnimationDefinitionsSize();i++)
			autoUsedIds.addAll(AnimationDefinitions.getDefs(i).getUsedSynthSoundIds());
		
		for (int i = 0;i < Utils.getObjectDefinitionsSize();i++) {
			ObjectDefinitions defs = ObjectDefinitions.getDefs(i);
			if (defs == null)
				continue;
			if (defs.ambientSoundId > 0 && !defs.midiSound)
				autoUsedIds.add(ObjectDefinitions.getDefs(i).ambientSoundId);
			if (defs.soundEffectsTimed != null && defs.soundEffectsTimed.length > 0 && !defs.midiSoundEffectsTimed)
				for (int sound : defs.soundEffectsTimed)
					if (sound > 0)
						autoUsedIds.add(sound);
		}
		
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.CS2_SCRIPTS).getLastArchiveId();i++) {
			CS2Script s = CS2Definitions.getScript(i);
			if (s == null)
				continue;
			for (int x = 0;x < s.operations.length;x++) {
				if (s.operations[x] == CS2Instruction.SOUND_SYNTH) {
					for (int op = 0;op < s.operations.length;op++) {
						if (s.operations[op] == CS2Instruction.SOUND_SYNTH && op-3 >= 0 && s.intOpValues[op-3] > 1)
							autoUsedIds.add(s.intOpValues[op-3]);
					}
					break;
				}
				if (s.operations[x] == CS2Instruction.SOUND_SYNTH_RATE) {
					for (int op = 0;op < s.operations.length;op++) {
						if (s.operations[op] == CS2Instruction.SOUND_SYNTH_RATE && op-5 >= 0 && s.intOpValues[op-5] > 1)
							autoUsedIds.add(s.intOpValues[op-5]);
					}
					break;
				}
				if (s.operations[x] == CS2Instruction.SOUND_SYNTH_VOLUME) {
					for (int op = 0;op < s.operations.length;op++) {
						if (s.operations[op] == CS2Instruction.SOUND_SYNTH_VOLUME && op-4 >= 0 && s.intOpValues[op-4] > 1)
							autoUsedIds.add(s.intOpValues[op-4]);
					}
					break;
				}

			}
		}
		
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.SOUND_EFFECTS).getLastArchiveId() + 1;i++) {
			SoundEffect effect = getEffect(i);
			if (effect == null)
				continue;
			if (autoUsedIds.contains(i))
				continue;
			System.out.println(i);
		}
		
		//Files.write(getEffect(168).toWAV(), new File("./one.wav"));
	}
	
	public static SoundEffect getEffect(int id) {
		byte[] data = Cache.STORE.getIndex(IndexType.SOUND_EFFECTS).getFile(id, 0);
		if (data == null)
			return null;
		return new SoundEffect(new InputStream(data));
	}
	
	public byte[] toWAV() {
		byte[] mixed = mix();
		
		/*
		 * Convert to 16 bit audio
		 */
		byte[] fixed = new byte[mixed.length*2];
		
		for (int i = 0;i < mixed.length;i++) {
			fixed[i * 2] = (byte) (mixed[i] >> 8 * 2);			
			fixed[i * 2 + 1] = (byte) (mixed[i] >> 16 * 2 + 1);
		}
		
		
		OutputStream stream = new OutputStream();
		stream.writeInt(0x52494646); /* 'RIFF' chunk name */
		stream.writeIntLE(36 + fixed.length); /* 'RIFF' block size (36 = 4 + 4 + 4 + 2 + 2 + 4 + 4 + 2 + 2 + 4 + 4) */
		stream.writeInt(0x57415645); /* 'WAVE' format */
		stream.writeInt(0x666d7420); /* 'fmt ' subchunk */
		stream.writeIntLE(16); /* 'fmt ' subchunk size (16 = 2 + 2 + 4 + 4 + 2 + 2) */
		stream.writeShortLE(1); /* audio format is 1 i.e. linear-quantized pulse modulation (see: http://en.wikipedia.org/wiki/Linear_PCM) */
		stream.writeShortLE(1); /* 1 audio channel, i.e. mono not stereo */
		stream.writeIntLE(22050); /* sample rate is 22050 Hz */
		stream.writeIntLE(22050); /* byte rate is 22050 Hz (each sample is 1 byte) */
		stream.writeShortLE(1); /* bytes per each sample for our 1 channel */
		stream.writeShortLE(16); /* 8 bits per sample */
		stream.writeInt(0x64617461); /* 'data' subchunk */
		stream.writeIntLE(fixed.length); /* encoded audio data */
	    stream.writeBytes(fixed);
	    return stream.toByteArray();
	}

	public final int getDelay() {
		int delay = 9999999;

		int i_2;
		for (i_2 = 0; i_2 < 10; i_2++) {
			if (this.instruments[i_2] != null && this.instruments[i_2].offset / 20 < delay) {
				delay = this.instruments[i_2].offset / 20;
			}
		}

		if (this.loopBegin < this.loopEnd && this.loopBegin / 20 < delay) {
			delay = this.loopBegin / 20;
		}

		if (delay != 9999999 && delay != 0) {
			for (i_2 = 0; i_2 < 10; i_2++) {
				if (this.instruments[i_2] != null) {
					this.instruments[i_2].offset -= delay * 20;
				}
			}

			if (this.loopBegin < this.loopEnd) {
				this.loopBegin -= delay * 20;
				this.loopEnd -= delay * 20;
			}

			return delay;
		} else {
			return 0;
		}
	}

	final byte[] mix() {
		int duration = 0;

		for (int i = 0; i < 10; i++) {
			if (this.instruments[i] != null && this.instruments[i].duration + this.instruments[i].offset > duration) {
				duration = this.instruments[i].duration + this.instruments[i].offset;
			}
		}

		if (duration == 0) {
			return new byte[0];
		} else {
			int ns = duration * 22050 / 1000;
			byte[] mixed = new byte[ns];

			for (int i = 0; i < 10; i++) {
				if (this.instruments[i] != null) {
					int mixDuration = this.instruments[i].duration * 22050 / 1000;
					int offset = this.instruments[i].offset * 22050 / 1000;
					int[] samples = this.instruments[i].synthesize(mixDuration, this.instruments[i].duration);

					for (int j = 0; j < mixDuration; j++) {
						int out = (samples[j] >> 8) + mixed[j + offset];
						if ((out + 128 & ~0xff) != 0) {
							out = out >> 31 ^ 0x7f;
						}

						mixed[j + offset] = (byte) out;
					}
				}
			}

			return mixed;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			result.append("  ");
			try {
				result.append(field.getType().getCanonicalName() + " " + field.getName() + ": ");
				result.append(Utils.getFieldValue(this, field));
			} catch (Throwable ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}
