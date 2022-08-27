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
package com.rs.cache.loaders.animations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class AnimationDefinitions {
	
	public int id;
	public int replayMode;
	public int[] interfaceFrames;
	public int[] frameDurations;
	public int[][] soundSettings;
	public int loopDelay = -1;
	public boolean[] aBoolArray5915;
	public int priority = -1;
	public int leftHandItem = 65535;
	public int rightHandItem = 65535;
	public int[] anIntArray5919;
	public int animatingPrecedence;
	public int walkingPrecedence;
	public int[] frameHashes;
	public int[] frameSetIds;
	public int[] anIntArray5923;
	public boolean aBool5923;
	public boolean tweened;
	public int[] soundDurations;
	public int[] anIntArray5927;
	public boolean vorbisSound;
	public int maxLoops = 1;
	public int[][] soundFlags;
	private AnimationFrameSet[] frameSets;
	
	public HashMap<Integer, Object> clientScriptMap = new HashMap<Integer, Object>();
	
	private static final ConcurrentHashMap<Integer, AnimationDefinitions> animDefs = new ConcurrentHashMap<Integer, AnimationDefinitions>();
	private static final HashMap<Integer, Integer> itemAnims = new HashMap<Integer, Integer>();
	
	public static void main(String[] args) throws IOException {
		Cache.init("../cache/");
		
		int[] itemIds = { 1351, 1349, 1353, 1361, 1355, 1357, 1359, 6739, 13661 };
		Map<Integer, Map<Integer, Integer>> mapping = new HashMap<>();
		
		for (int i = 0;i < Utils.getAnimationDefinitionsSize();i++) {
			AnimationDefinitions def = AnimationDefinitions.getDefs(i);
			for (int itemId : itemIds) {
				if (def.rightHandItem == itemId || def.leftHandItem == itemId) {
					Map<Integer, Integer> sets = mapping.get(itemId);
					if (sets == null)
						sets = new HashMap<>();
					
					int hash = Arrays.hashCode(def.frameHashes);
					sets.put(hash, def.id);
					mapping.put(itemId, sets);
				}
			}
		}
		
//		AnimationDefinitions def = AnimationDefinitions.getDefs(12322);
//		System.out.println(def);
	}
	
	public static void init() {
		for (int i = 0; i < Utils.getAnimationDefinitionsSize(); i++) {
			AnimationDefinitions defs = getDefs(i);
			if (defs == null)
				continue;
			if (defs.leftHandItem != -1 && defs.leftHandItem != 65535) {
				itemAnims.put(defs.leftHandItem, i);
			}
			if (defs.rightHandItem != -1 && defs.rightHandItem != 65535) {
				itemAnims.put(defs.rightHandItem, i);
			}
		}
	}
	
	public AnimationFrameSet[] getFrameSets() {
		if (frameSets == null) {
			frameSets = new AnimationFrameSet[frameSetIds.length];
			if (frameSetIds != null) {
				for (int i = 0;i < frameSetIds.length;i++) {
					frameSets[i] = AnimationFrameSet.getFrameSet(frameSetIds[i]);
				}
			}
		}
		return frameSets;
	}
	
	public static int getAnimationWithItem(int itemId) {
		if (itemAnims.get(itemId) != null)
			return itemAnims.get(itemId);
		return -1;
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

	public static final AnimationDefinitions getDefs(int emoteId) {
		try {
			AnimationDefinitions defs = animDefs.get(emoteId);
			if (defs != null)
				return defs;
			byte[] data = Cache.STORE.getIndex(IndexType.ANIMATIONS).getFile(ArchiveType.ANIMATIONS.archiveId(emoteId), ArchiveType.ANIMATIONS.fileId(emoteId));
			defs = new AnimationDefinitions();
			if (data != null)
				defs.readValueLoop(new InputStream(data));
			defs.method11143();
			defs.id = emoteId;
			animDefs.put(emoteId, defs);
			return defs;
		} catch (Throwable t) {
			return null;
		}
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public int getEmoteTime() {
		if (frameDurations == null)
			return 0;
		int ms = 0;
		for (int i : frameDurations)
			ms += i * 20;
		return ms;
	}
	
	public int getEmoteGameTicks() {
		return getEmoteTime() / 600;
	}

	private void readValues(InputStream stream, int opcode) {
		if (1 == opcode) {
			int frameCount = stream.readUnsignedShort();
			frameDurations = new int[frameCount];
			for (int i = 0; i < frameCount; i++)
				frameDurations[i] = stream.readUnsignedShort();
			frameHashes = new int[frameCount];
			frameSetIds = new int[frameCount];
			for (int i = 0; i < frameCount; i++) {
				frameHashes[i] = stream.readUnsignedShort();
			}
			for (int i = 0; i < frameCount; i++)
				frameHashes[i] += (stream.readUnsignedShort() << 16);
			for (int i = 0; i < frameCount; i++) {
				frameSetIds[i] = frameHashes[i] >>> 16;
			}
		} else if (opcode == 2)
			loopDelay = stream.readUnsignedShort();
		else if (3 == opcode) {
			aBoolArray5915 = new boolean[256];
			int i_7_ = stream.readUnsignedByte();
			for (int i_8_ = 0; i_8_ < i_7_; i_8_++)
				aBoolArray5915[stream.readUnsignedByte()] = true;
		} else if (5 == opcode)
			priority = stream.readUnsignedByte();
		else if (6 == opcode)
			leftHandItem = stream.readUnsignedShort();
		else if (opcode == 7)
			rightHandItem = stream.readUnsignedShort();
		else if (8 == opcode)
			maxLoops = stream.readUnsignedByte();
		else if (9 == opcode)
			animatingPrecedence = stream.readUnsignedByte();
		else if (10 == opcode)
			walkingPrecedence = stream.readUnsignedByte();
		else if (opcode == 11)
			replayMode = stream.readUnsignedByte();
		else if (opcode == 12) {
			int i_9_ = stream.readUnsignedByte();
			interfaceFrames = new int[i_9_];
			for (int i_10_ = 0; i_10_ < i_9_; i_10_++)
				interfaceFrames[i_10_] = stream.readUnsignedShort();
			for (int i_11_ = 0; i_11_ < i_9_; i_11_++)
				interfaceFrames[i_11_] = (stream.readUnsignedShort() << 16) + interfaceFrames[i_11_];
		} else if (13 == opcode) {
			int i_12_ = stream.readUnsignedShort();
			soundSettings = new int[i_12_][];
			soundFlags = new int[i_12_][];
			for (int i_13_ = 0; i_13_ < i_12_; i_13_++) {
				int i_14_ = stream.readUnsignedByte();
				if (i_14_ > 0) {
					soundSettings[i_13_] = new int[i_14_];
					soundSettings[i_13_][0] = stream.read24BitInt();
					soundFlags[i_13_] = new int[3];
					soundFlags[i_13_][0] = soundSettings[i_13_][0] >> 8;
					soundFlags[i_13_][1] = soundSettings[i_13_][0] >> 5 & 0x7;
					soundFlags[i_13_][2] = soundSettings[i_13_][0] & 0x1f;
					for (int i_15_ = 1; i_15_ < i_14_; i_15_++)
						soundSettings[i_13_][i_15_] = stream.readUnsignedShort();
				}
			}
		} else if (opcode == 14)
			aBool5923 = true;
		else if (opcode == 15)
			tweened = true;
		else if (opcode != 16) {
			if (18 == opcode)
				vorbisSound = true;
			else if (19 == opcode) {
				if (soundDurations == null) {
					soundDurations = new int[soundSettings.length];
					for (int i_16_ = 0; i_16_ < soundSettings.length; i_16_++)
						soundDurations[i_16_] = 255;
				}
				soundDurations[stream.readUnsignedByte()] = stream.readUnsignedByte();
			} else if (opcode == 20) {
				if (null == anIntArray5927 || null == anIntArray5919) {
					anIntArray5927 = new int[soundSettings.length];
					anIntArray5919 = new int[soundSettings.length];
					for (int i_17_ = 0; i_17_ < soundSettings.length; i_17_++) {
						anIntArray5927[i_17_] = 256;
						anIntArray5919[i_17_] = 256;
					}
				}
				int i_18_ = stream.readUnsignedByte();
				anIntArray5927[i_18_] = stream.readUnsignedShort();
				anIntArray5919[i_18_] = stream.readUnsignedShort();
			} else if (249 == opcode) {
				int i_19_ = stream.readUnsignedByte();
				for (int i_21_ = 0; i_21_ < i_19_; i_21_++) {
					boolean bool = stream.readUnsignedByte() == 1;
					int i_22_ = stream.read24BitInt();
					if (bool)
						clientScriptMap.put(i_22_,  stream.readString());
					else
						clientScriptMap.put(i_22_,  stream.readInt());
				}
			}
		}
	}
	
	public Set<Integer> getUsedSynthSoundIds() {
		Set<Integer> set = new HashSet<>();
		if (this.vorbisSound || this.soundFlags == null || this.soundFlags.length <= 0)
			return set;
		for (int i = 0;i < soundFlags.length;i++) {
			if (soundFlags[i] == null)
				continue;
			set.add(soundFlags[i][0]);
		}
		return set;
	}
	
	void method11143() {
		if (animatingPrecedence == -1) {
			if (aBoolArray5915 != null)
				animatingPrecedence = 2;
			else
				animatingPrecedence = 0;
		}
		if (walkingPrecedence == -1) {
			if (null != aBoolArray5915)
				walkingPrecedence = 2;
			else
				walkingPrecedence = 0;
		}
	}
}
