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
package com.rs.cache.loaders;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.game.VarManager;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.util.Utils;

@SuppressWarnings("unused")
public class ObjectDefinitions {

	private static final ConcurrentHashMap<Integer, ObjectDefinitions> objectDefinitions = new ConcurrentHashMap<Integer, ObjectDefinitions>();

	public int anInt5633;
	public byte aByte5634;
	public int offsetY;
	public ObjectType[] types;
	public int[][] modelIds;
	private String name = "null";
	public int scaleY;
	public short[] modifiedColors;
	public byte[] aByteArray5641;
	public byte aByte5642;
	public short[] modifiedTextures;
	public byte aByte5644;
	public short[] originalColors;
	public byte aByte5646;
	public String[] options = new String[5];
	public int sizeX;
	public int sizeY;
	public int[] transformTo;
	public int interactable;
	public int ambientSoundId;
	public int anInt5654;
	public boolean delayShading;
	public int occludes;
	public boolean castsShadow;
	public int anInt5658;
	public int[] animations;
	public boolean members;
	public int decorDisplacement;
	public int varp;
	public int contrast;
	public boolean blocks;
	public int anInt5665;
	public int anInt5666;
	public int anInt5667;
	public int mapIcon;
	public int anInt5670;
	public boolean adjustMapSceneRotation;
	public int mapSpriteRotation;
	public boolean flipMapSprite;
	public boolean inverted;
	public int[] animProbs;
	public int scaleX;
	public int clipType;
	public int scaleZ;
	public int offsetX;
	public short[] originalTextures;
	public int offsetZ;
	public int anInt5682;
	public int anInt5683;
	public int anInt5684;
	public boolean obstructsGround;
	public boolean ignoreAltClip;
	public int supportsItems;
	public int[] audioTracks;
	public int mapSpriteId;
	public int varpBit;
	public static short[] aShortArray5691 = new short[256];
	public int ambient;
	public int ambientSoundHearDistance;
	public int anInt5694;
	public int ambientSoundVolume;
	public boolean aBool5696;
	public byte groundContoured;
	public int anInt5698;
	public boolean aBool5699;
	public boolean aBool5700;
	public boolean hidden;
	public boolean aBool5702;
	public boolean aBool5703;
	public int anInt5704;
	public int anInt5705;
	public boolean hasAnimation;
	public int[] anIntArray5707;
	public int anInt5708;
	public int anInt5709;
	public int anInt5710;
	public boolean aBool5711;
	public HashMap<Integer, Object> parameters;
	public int id;
	public int accessBlockFlag;

	public static void main(String[] args) throws IOException {
		//Cache.init();
		ObjectDefinitions defs = getDefs(782);
		System.out.println(defs);
		
//		for (int i = 0;i < Utils.getObjectDefinitionsSize();i++) {
//			ObjectDefinitions def = getDefs(i);
//			if (def.getName().contains("Potter") && def.getName().toLowerCase().contains("oven")) {
//				System.out.println(def.getName());
//			}
//		}
//		
//		for (int i = 0;i < defs.toObjectIds.length;i++) {
//			ObjectDefinitions toDef = getObjectDefinitions(defs.toObjectIds[i]);
//			System.out.println(i+"-"+toDef.getName());
//		}
		
//		for (int i = 0;i < Utils.getObjectDefinitionsSize();i++) {
//			ObjectDefinitions defs = getObjectDefinitions(i);
//			if (defs.configFileId == 8405)
//				System.out.println(defs);
//		}
		
//		ProductInfo[] infos = new ProductInfo[] { ProductInfo.Hammerstone, ProductInfo.Asgarnian, ProductInfo.Yanillian, ProductInfo.Krandorian, ProductInfo.Wildblood, ProductInfo.Barley, ProductInfo.Jute };
//		
//		int prodIdx = 0;
//		for (ProductInfo info : infos) {
//			int count = 0;
//			int startIdx = 0;
//			for (int i = 0;i < 64;i++) {
//				ObjectDefinitions toDef = getObjectDefinitions(defs.toObjectIds[i]);
//				if (toDef != null && toDef.getName().contains(info.name())) {
//					if (startIdx == 0)
//						startIdx = i;
//					count++;
//				}
//			}
//			System.out.println(prodIdx + "("+info.name() + ") " + (count-info.maxStage) + "-" + startIdx);
//			
//			prodIdx++;
//		}
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
	
	public int[] getModels(ObjectType type) {
		for (int i = 0;i < types.length;i++) {
			if (types[i] == type)
				return modelIds[i];
		}
		return new int[] {0};
	}
	
	public String getFirstOption() {
		if (options == null || options.length < 1 || options[0] == null)
			return "";
		return options[0];
	}

	public String getSecondOption() {
		if (options == null || options.length < 2 || options[1] == null)
			return "";
		return options[1];
	}

	public String getOption(int option) {
		if (options == null || options.length < option || option == 0 || options[option - 1] == null)
			return "";
		return options[option - 1];
	}
	
	public String getOption(ClientPacket option) {
		int op = -1;
		switch(option) {
			case OBJECT_OP1 -> op = 0;
			case OBJECT_OP2 -> op = 1;
			case OBJECT_OP3 -> op = 2;
			case OBJECT_OP4 -> op = 3;
			case OBJECT_OP5 -> op = 4;
			default -> throw new IllegalArgumentException("Unexpected value: " + option);
		}
		if (options == null || op < 0 || options.length < op || options[op] == null)
			return "";
		return options[op];
	}

	public String getThirdOption() {
		if (options == null || options.length < 3 || options[2] == null)
			return "";
		return options[2];
	}

	public boolean containsOption(int i, String option) {
		if (options == null || options[i] == null || options.length <= i)
			return false;
		return options[i].equals(option);
	}
	
	public boolean containsOptionIgnoreCase(String string) {
		if (options == null)
			return false;
		for (String option : options) {
			if (option == null)
				continue;
			if (option.toLowerCase().contains(string.toLowerCase()))
				return true;
		}
		return false;
	}

	public boolean containsOption(String o) {
		if (options == null)
			return false;
		for (String option : options) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase(o))
				return true;
		}
		return false;
	}
	
	public boolean containsOption(int i) {
		if (options == null || options[i] == null || options.length <= i)
			return false;
		return !options[i].equals("null");
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			int i_4_ = stream.readUnsignedByte();
			types = new ObjectType[i_4_];
			modelIds = new int[i_4_][];
			for (int i_5_ = 0; i_5_ < i_4_; i_5_++) {
				types[i_5_] = ObjectType.forId(stream.readByte());
				int i_6_ = stream.readUnsignedByte();
				modelIds[i_5_] = new int[i_6_];
				for (int i_7_ = 0; i_7_ < i_6_; i_7_++)
					modelIds[i_5_][i_7_] = stream.readBigSmart();
			}
		} else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 14)
			sizeX = stream.readUnsignedByte();
		else if (15 == opcode)
			sizeY = stream.readUnsignedByte();
		else if (17 == opcode) {
			clipType = 0;
			blocks = false;
		} else if (18 == opcode)
			blocks = false;
		else if (opcode == 19)
			interactable = stream.readUnsignedByte();
		else if (21 == opcode)
			groundContoured = (byte) 1;
		else if (22 == opcode)
			delayShading = true;
		else if (opcode == 23)
			occludes = 1;
		else if (opcode == 24) {
			int i_8_ = stream.readBigSmart();
			if (i_8_ != -1)
				animations = new int[] { i_8_ };
		} else if (opcode == 27)
			clipType = 1;
		else if (opcode == 28)
			decorDisplacement = (stream.readUnsignedByte() << 2);
		else if (opcode == 29)
			ambient = stream.readByte();
		else if (39 == opcode)
			contrast = stream.readByte();
		else if (opcode >= 30 && opcode < 35)
			options[opcode - 30] = stream.readString();
		else if (40 == opcode) {
			int i_9_ = stream.readUnsignedByte();
			originalColors = new short[i_9_];
			modifiedColors = new short[i_9_];
			for (int i_10_ = 0; i_10_ < i_9_; i_10_++) {
				originalColors[i_10_] = (short) stream.readUnsignedShort();
				modifiedColors[i_10_] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int i_11_ = stream.readUnsignedByte();
			originalTextures = new short[i_11_];
			modifiedTextures = new short[i_11_];
			for (int i_12_ = 0; i_12_ < i_11_; i_12_++) {
				originalTextures[i_12_] = (short) stream.readUnsignedShort();
				modifiedTextures[i_12_] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int i_13_ = stream.readUnsignedByte();
			aByteArray5641 = new byte[i_13_];
			for (int i_14_ = 0; i_14_ < i_13_; i_14_++)
				aByteArray5641[i_14_] = (byte) stream.readByte();
		} else if (opcode == 62)
			inverted = true;
		else if (opcode == 64)
			castsShadow = false;
		else if (65 == opcode)
			scaleX = stream.readUnsignedShort();
		else if (opcode == 66)
			scaleY = stream.readUnsignedShort();
		else if (67 == opcode)
			scaleZ = stream.readUnsignedShort();
		else if (opcode == 69)
			accessBlockFlag = stream.readUnsignedByte();
		else if (70 == opcode)
			offsetX = (stream.readShort() << 2);
		else if (opcode == 71)
			offsetY = (stream.readShort() << 2);
		else if (opcode == 72)
			offsetZ = (stream.readShort() << 2);
		else if (73 == opcode)
			obstructsGround = true;
		else if (opcode == 74)
			ignoreAltClip = true;
		else if (opcode == 75)
			supportsItems = stream.readUnsignedByte();
		else if (77 == opcode || 92 == opcode) {
			varpBit = stream.readUnsignedShort();
			if (65535 == varpBit)
				varpBit = -1;
			varp = stream.readUnsignedShort();
			if (varp == 65535)
				varp = -1;
			int objectId = -1;
			if (opcode == 92)
				objectId = stream.readBigSmart();
			int transforms = stream.readUnsignedByte();
			transformTo = new int[transforms + 2];
			for (int i = 0; i <= transforms; i++)
				transformTo[i] = stream.readBigSmart();
			transformTo[1 + transforms] = objectId;
		} else if (78 == opcode) {
			ambientSoundId = stream.readUnsignedShort();
			ambientSoundHearDistance = stream.readUnsignedByte();
		} else if (79 == opcode) {
			anInt5667 = stream.readUnsignedShort();
			anInt5698 = stream.readUnsignedShort();
			ambientSoundHearDistance = stream.readUnsignedByte();
			int i_18_ = stream.readUnsignedByte();
			audioTracks = new int[i_18_];
			for (int i_19_ = 0; i_19_ < i_18_; i_19_++)
				audioTracks[i_19_] = stream.readUnsignedShort();
		} else if (81 == opcode) {
			groundContoured = (byte) 2;
			anInt5654 = stream.readUnsignedByte() * 256;
		} else if (opcode == 82)
			hidden = true;
		else if (88 == opcode)
			aBool5703 = false;
		else if (opcode == 89)
			aBool5702 = false;
		else if (91 == opcode)
			members = true;
		else if (93 == opcode) {
			groundContoured = (byte) 3;
			anInt5654 = stream.readUnsignedShort();
		} else if (opcode == 94)
			groundContoured = (byte) 4;
		else if (95 == opcode) {
			groundContoured = (byte) 5;
			anInt5654 = stream.readShort();
		} else if (97 == opcode)
			adjustMapSceneRotation = true;
		else if (98 == opcode)
			hasAnimation = true;
		else if (99 == opcode) {
			anInt5705 = stream.readUnsignedByte();
			anInt5665 = stream.readUnsignedShort();
		} else if (opcode == 100) {
			anInt5670 = stream.readUnsignedByte();
			anInt5666 = stream.readUnsignedShort();
		} else if (101 == opcode)
			mapSpriteRotation = stream.readUnsignedByte();
		else if (opcode == 102)
			mapSpriteId = stream.readUnsignedShort();
		else if (opcode == 103)
			occludes = 0;
		else if (104 == opcode)
			ambientSoundVolume = stream.readUnsignedByte();
		else if (opcode == 105)
			flipMapSprite = true;
		else if (106 == opcode) {
			int i_20_ = stream.readUnsignedByte();
			int i_21_ = 0;
			animations = new int[i_20_];
			animProbs = new int[i_20_];
			for (int i_22_ = 0; i_22_ < i_20_; i_22_++) {
				animations[i_22_] = stream.readBigSmart();
				i_21_ += animProbs[i_22_] = stream.readUnsignedByte();
			}
			for (int i_23_ = 0; i_23_ < i_20_; i_23_++)
				animProbs[i_23_] = animProbs[i_23_] * 65535 / i_21_;
		} else if (opcode == 107)
			mapIcon = stream.readUnsignedShort();
		else if (opcode >= 150 && opcode < 155) {
			options[opcode - 150] = stream.readString();
//			if (!((ObjectDefinitionsLoader) loader).showOptions)
//				aStringArray5647[opcode - 150] = null;
		} else if (160 == opcode) {
			int i_24_ = stream.readUnsignedByte();
			anIntArray5707 = new int[i_24_];
			for (int i_25_ = 0; i_25_ < i_24_; i_25_++)
				anIntArray5707[i_25_] = stream.readUnsignedShort();
		} else if (162 == opcode) {
			groundContoured = (byte) 3;
			anInt5654 = stream.readInt();
		} else if (163 == opcode) {
			aByte5644 = (byte) stream.readByte();
			aByte5642 = (byte) stream.readByte();
			aByte5646 = (byte) stream.readByte();
			aByte5634 = (byte) stream.readByte();
		} else if (164 == opcode)
			anInt5682 = stream.readShort();
		else if (165 == opcode)
			anInt5683 = stream.readShort();
		else if (166 == opcode)
			anInt5710 = stream.readShort();
		else if (167 == opcode)
			anInt5704 = stream.readUnsignedShort();
		else if (168 == opcode)
			aBool5696 = true;
		else if (169 == opcode)
			aBool5700 = true;
		else if (opcode == 170)
			anInt5684 = stream.readUnsignedSmart();
		else if (opcode == 171)
			anInt5658 = stream.readUnsignedSmart();
		else if (opcode == 173) {
			anInt5708 = stream.readUnsignedShort();
			anInt5709 = stream.readUnsignedShort();
		} else if (177 == opcode)
			aBool5699 = true;
		else if (178 == opcode)
			anInt5694 = stream.readUnsignedByte();
		else if (189 == opcode)
			aBool5711 = true;
		else if (249 == opcode) {
			int length = stream.readUnsignedByte();
			if (parameters == null)
				parameters = new HashMap<Integer, Object>(length);
			for (int i_60_ = 0; i_60_ < length; i_60_++) {
				boolean bool = stream.readUnsignedByte() == 1;
				int i_61_ = stream.read24BitInt();
				if (!bool)
					parameters.put(i_61_, stream.readInt());
				else
					parameters.put(i_61_, stream.readString());
			}
		}
	}
	
	public int getIdForPlayer(VarManager vars) {
		if (transformTo == null || transformTo.length == 0)
			return id;
		if (vars == null) {
			int varIdx = transformTo[transformTo.length - 1];
			return varIdx;
		}
		int index = -1;
		if (varpBit != -1) {
			index = vars.getVarBit(varpBit);
		} else if (varp != -1) {
			index = vars.getVar(varp);
		}
		if (index >= 0 && index < transformTo.length - 1 && transformTo[index] != -1) {
			return transformTo[index];
		} else {
			int varIdx = transformTo[transformTo.length - 1];
			return varIdx;
		}
	}
	
	public String getConfigInfoString() {
		String finalString = "";
		String transforms = "\r\n";
		boolean found = false;
		for (int objectId = 0;objectId < Utils.getObjectDefinitionsSize();objectId++) {
			ObjectDefinitions defs = getDefs(objectId);
			if (defs.transformTo == null)
				continue;
			for (int i = 0;i < defs.transformTo.length;i++) {
				if (defs.transformTo[i] == id) {
					found = true;
					transforms += "[" + objectId + "("+defs.getName()+")" +":";
					if (defs.varp != -1)
						transforms += ("v"+defs.varp+"="+i);
					if (defs.varpBit != -1)
						transforms += ("vb"+defs.varpBit+"="+i);
					transforms += "], \r\n";
				}
			}
		}
		if (found) {
			finalString += " - transformed into by: " + transforms;
			transforms = "";
		}
		found = false;
		if (transformTo != null) {
			found = true;
			for (int i = 0;i < transformTo.length;i++) {
				if (transformTo[i] != -1)
					transforms += "[" + i + ": " + transformTo[i] +" ("+getDefs(transformTo[i]).name+")";
				else
					transforms += "["+i+": INVISIBLE";
				transforms += "], \r\n";
			}
		}
		if (found) {
			finalString += " - transforms into with ";
			if (varp != -1)
				finalString += ("v"+varp) + ":";
			if (varpBit != -1)
				finalString += ("vb"+varpBit) + ":";
			finalString += transforms;
			transforms = "";
		}
		return finalString;
	}

	private void skipReadModelIds(InputStream stream) {
		int length = stream.readUnsignedByte();
		for (int index = 0; index < length; index++) {
			stream.skip(1);
			int length2 = stream.readUnsignedByte();
			for (int i = 0; i < length2; i++)
				stream.readBigSmart();
		}
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	private ObjectDefinitions() {
		aByte5634 = (byte) 0;
		sizeX = 1;
		sizeY = 1;
		clipType = 2;
		blocks = true;
		interactable = -1;
		groundContoured = (byte) 0;
		anInt5654 = -1;
		delayShading = false;
		occludes = -1;
		anInt5684 = 960;
		anInt5658 = 0;
		animations = null;
		animProbs = null;
		decorDisplacement = 64;
		ambient = 0;
		contrast = 0;
		anInt5665 = -1;
		anInt5666 = -1;
		anInt5705 = -1;
		anInt5670 = -1;
		mapIcon = -1;
		mapSpriteId = -1;
		adjustMapSceneRotation = false;
		mapSpriteRotation = 0;
		flipMapSprite = false;
		inverted = false;
		castsShadow = true;
		scaleX = 128;
		scaleY = 128;
		scaleZ = 128;
		offsetX = 0;
		offsetY = 0;
		offsetZ = 0;
		anInt5682 = 0;
		anInt5683 = 0;
		anInt5710 = 0;
		obstructsGround = false;
		ignoreAltClip = false;
		supportsItems = -1;
		anInt5704 = 0;
		varpBit = -1;
		varp = -1;
		ambientSoundId = -1;
		ambientSoundHearDistance = 0;
		anInt5694 = 0;
		ambientSoundVolume = -1;
		aBool5696 = false;
		anInt5667 = 0;
		anInt5698 = 0;
		aBool5700 = false;
		aBool5702 = true;
		hidden = false;
		aBool5703 = true;
		members = false;
		hasAnimation = false;
		anInt5708 = -1;
		anInt5709 = 0;
		accessBlockFlag = 0;
		aBool5699 = false;
		aBool5711 = false;
		name = "null";
	}

	public static ObjectDefinitions getDefs(int id) {
		ObjectDefinitions def = objectDefinitions.get(id);
		if (def == null) {
			def = new ObjectDefinitions();
			def.id = id;
			byte[] data = Cache.STORE.getIndex(IndexType.OBJECTS).getFile(ArchiveType.OBJECTS.archiveId(id), ArchiveType.OBJECTS.fileId(id));
			if (data != null)
				def.readValueLoop(new InputStream(data));
			def.method7966();
//			if (def.ignoreAltClip) {
//				def.clipType = 0;
//				def.blocks = false;
//			}
			/*
			 * DUNGEONEERING DOORS?..
			 */
			switch (id) {
			case 50342:
			case 50343:
			case 50344:
			case 53948:
			case 55762:
			case 50350:
			case 50351:
			case 50352:
			case 53950:
			case 55764:
				def.ignoreAltClip = false;
				def.blocks = true;
				def.clipType = 1;
				break;
			}
			objectDefinitions.put(id, def);
		}
		return def;
	}
	
	void method7966() {
		if (interactable == -1) {
			interactable = 0;
			if (null != types && types.length == 1 && (types[0] == ObjectType.SCENERY_INTERACT))
				interactable = 1;
			for (int i_30_ = 0; i_30_ < 5; i_30_++) {
				if (options[i_30_] != null) {
					interactable = 1;
					break;
				}
			}
		}
		if (supportsItems == -1)
			supportsItems =  (0 != clipType ? 1 : 0);
		if (animations != null || hasAnimation || transformTo != null)
			aBool5699 = true;
	}
	
	public static ObjectDefinitions getDefs(int id, VarManager player) {
		ObjectDefinitions defs = getDefs(getDefs(id).getIdForPlayer(player));
		if (defs == null)
			return getDefs(id);
		return defs;
	}
	
	public String getName() {
		return getName(null);
	}
	
	public String getName(VarManager player) {
		int realId = getIdForPlayer(player);
		if (realId == id)
			return name;
		if (realId == -1)
			return "null";
		return getDefs(realId).name;
	}
	

	public ObjectDefinitions getRealDefs() {
		return getDefs(getIdForPlayer(null));
	}

	public int getClipType() {
		return clipType;
	}

	public boolean blocks() {
		return blocks;
	}

	public boolean isClipped() {
		return clipType != 0;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}
	
	public int getAccessBlockFlag() {
		return accessBlockFlag;
	}

	public static void clearObjectDefinitions() {
		objectDefinitions.clear();
	}

	public ObjectType getType(int i) {
		if (types != null && i < types.length)
			return types[i];
		return ObjectType.SCENERY_INTERACT;
	}

	public int getSlot() {
		return getType(0).slot;
	}

}
