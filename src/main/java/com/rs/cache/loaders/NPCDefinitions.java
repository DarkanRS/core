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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.cache.loaders.animations.AnimationDefinitions;
import com.rs.lib.game.VarManager;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public final class NPCDefinitions {
	
	private static final ConcurrentHashMap<Integer, NPCDefinitions> MAP = new ConcurrentHashMap<Integer, NPCDefinitions>();

	public int id;
	public HashMap<Integer, Object> parameters;
	public int anInt4856;
	public String[] options = new String[5];
	public String[] membersOptions = new String[5];
	public int[] modelIds;
	public byte aByte4916 = -1;
	private String name = "null";
	public int size = 1;
	public int basId = -1;
	byte aByte4871 = 0;
	public int anInt4873 = -1;
	public int anInt4861 = -1;
	public int anInt4875 = -1;
	public int anInt4854 = -1;
	public int attackOpCursor = -1;
	public boolean drawMapdot = true;
	public int combatLevel = -1;
	int resizeX = 128;
	int resizeY = 128;
	public boolean aBool4904 = false;
	public boolean aBool4890 = false;
	public boolean aBool4884 = false;
	int ambient = 0;
	int contrast = 0;
	public int headIcons = -1;
	public int armyIcon = -1;
	public int rotation = 32;
	public int varpBit = -1;
	public int varp = -1;
	public boolean visible = true;
	public boolean isClickable = true;
	public boolean animateIdle = true;
	public short aShort4874 = 0;
	public short aShort4897 = 0;
	public byte aByte4883 = -96;
	public byte aByte4899 = -16;
	public byte walkMask = 0;
	public int walkingAnimation = -1;
	public int rotate180Animation = -1;
	public int rotate90RightAnimation = -1;
	public int rotate90LeftAnimation = -1;
	public int specialByte = 0;
	public int anInt4908 = 0;
	public int anInt4909 = 255;
	public int height = -1;
	public int mapIcon = -1;
	public int anInt4917 = -1;
	public int anInt4911 = 256;
	public int anInt4919 = 256;
	public int anInt4913 = 0;
	public boolean aBool4920 = true;
	short[] originalColors;
	public short[] modifiedColors;
	short[] originalTextures;
	public short[] modifiedTextures;
	byte[] recolourPalette;
	public int[] headModels;
	public int[] transformTo;
	int[][] modelTranslation;
	private int[] bonuses;
	private int[] strBonuses;
	byte aByte4868;
	byte aByte4869;
	byte aByte4905;
	public int[] quests;
	public boolean aBool4872;
	public MovementType movementType;
	public int respawnDirection = 4;
	
	private boolean usesCrawlWalkBAS;
	
	public static void main(String[] args) throws IOException {
		Cache.init("../cache/");
		File file = new File("npcModifiedColorsTextures.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append("//Version = 727\n");
		writer.flush();
		for (int i = 0;i < Utils.getNPCDefinitionsSize();i++) {
			NPCDefinitions defs = getDefs(i);
			if (defs.modifiedColors != null || defs.modifiedTextures != null) {
				writer.append(i + ": " + defs.getName());
				writer.newLine();
				writer.append("Models:" + Arrays.toString(defs.modelIds));
				writer.newLine();
				writer.append("Colors: " + Arrays.toString(defs.modifiedColors)); 
				writer.newLine();
				writer.append("Textures: " + Arrays.toString(defs.modifiedTextures));
				writer.newLine();
				writer.flush();
			}
		}
		writer.close();
	}
	
	public boolean transformsTo(int id) {
		if (this.transformTo == null)
			return false;
		for (int to : transformTo) {
			if (to == id)
				return true;
		}
		return false;
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

	public static final NPCDefinitions getDefs(int id) {
		NPCDefinitions def = MAP.get(id);
		if (def == null) {
			def = new NPCDefinitions(id);
			def.setEmptyModelIds();
			byte[] data = Cache.STORE.getIndex(IndexType.NPCS).getFile(ArchiveType.NPCS.archiveId(id), ArchiveType.NPCS.fileId(id));
			if (data == null) {
				// System.out.println("Failed loading NPC " + id + ".");
			} else
				def.readValueLoop(new InputStream(data));
			if (def.basId != -1) {
				BASDefinitions render = BASDefinitions.getDefs(def.basId);
				if (render != null && render.walkAnimation == -1 && render.teleportingAnimation != -1)
					def.usesCrawlWalkBAS = true;
			}
			def.bonuses = new int[10];
			def.bonuses[0] = def.getStabAtt();
			def.bonuses[1] = def.getSlashAtt();
			def.bonuses[2] = def.getCrushAtt();
			def.bonuses[3] = def.getMagicAtt();
			def.bonuses[4] = def.getRangeAtt();
			def.bonuses[5] = def.getStabDef();
			def.bonuses[6] = def.getSlashDef();
			def.bonuses[7] = def.getCrushDef();
			def.bonuses[8] = def.getMagicDef();
			def.bonuses[9] = def.getRangeDef();
			
			def.strBonuses = new int[3];
			def.strBonuses[0] = def.getMeleeStr();
			def.strBonuses[1] = def.getRangeStr();
			def.strBonuses[2] = def.getMagicStr();
			MAP.put(id, def);
		}
		return def;
	}

	public void setEmptyModelIds() {
		if (modelIds == null)
			modelIds = new int[0];
	}

	private void readValueLoop(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public int getId() {
		return id;
	}
	
	public List<Integer> getCompatibleAnimations() {
		HashSet<Integer> animsWithSkeleton = new HashSet<Integer>();
		if (basId != -1) {
			if (BASDefinitions.getDefs(basId).standAnimation != -1) {
				if (AnimationDefinitions.getDefs(BASDefinitions.getDefs(basId).standAnimation).frameSetIds != null) {
					int skeleton = AnimationDefinitions.getDefs(BASDefinitions.getDefs(basId).standAnimation).frameSetIds[0];
					for (int i = 0; i < Utils.getAnimationDefinitionsSize(); i++) {
						AnimationDefinitions check = AnimationDefinitions.getDefs(i);
						if (check == null)
							continue;
						if (check.frameSetIds == null || check.frameSetIds[0] == -1)
							continue;
						if (check.frameSetIds[0] == skeleton)
							animsWithSkeleton.add(i);
					}
				}
			}
		}
		List<Integer> list = new ArrayList<>(animsWithSkeleton);
		Collections.sort(list);
		return list;
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			int i_4 = stream.readUnsignedByte();
			this.modelIds = new int[i_4];
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.modelIds[i_5] = stream.readBigSmart();
			}
		} else if (opcode == 2) {
			this.name = stream.readString();
		} else if (opcode == 12) {
			this.size = stream.readUnsignedByte();
		} else if (opcode >= 30 && opcode < 35) {
			this.options[opcode - 30] = stream.readString();
		} else if (opcode == 40) {
			int i_4 = stream.readUnsignedByte();
			this.originalColors = new short[i_4];
			this.modifiedColors = new short[i_4];
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.originalColors[i_5] = (short) stream.readUnsignedShort();
				this.modifiedColors[i_5] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int i_4 = stream.readUnsignedByte();
			this.originalTextures = new short[i_4];
			this.modifiedTextures = new short[i_4];
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.originalTextures[i_5] = (short) stream.readUnsignedShort();
				this.modifiedTextures[i_5] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int i_4 = stream.readUnsignedByte();
			this.recolourPalette = new byte[i_4];
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.recolourPalette[i_5] = (byte) stream.readByte();
			}
		} else if (opcode == 60) {
			int i_4 = stream.readUnsignedByte();
			this.headModels = new int[i_4];
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.headModels[i_5] = stream.readBigSmart();
			}
		} else if (opcode == 93) {
			this.drawMapdot = false;
		} else if (opcode == 95) {
			this.combatLevel = stream.readUnsignedShort();
		} else if (opcode == 97) {
			this.resizeX = stream.readUnsignedShort();
		} else if (opcode == 98) {
			this.resizeY = stream.readUnsignedShort();
		} else if (opcode == 99) {
			this.aBool4904 = true;
		} else if (opcode == 100) {
			this.ambient = stream.readByte();
		} else if (opcode == 101) {
			this.contrast = stream.readByte();
		} else if (opcode == 102) {
			this.headIcons = stream.readUnsignedShort();
		} else if (opcode == 103) {
			this.rotation = stream.readUnsignedShort();
		} else if (opcode == 106 || opcode == 118) {
			this.varpBit = stream.readUnsignedShort();
			if (this.varpBit == 65535) {
				this.varpBit = -1;
			}
			this.varp = stream.readUnsignedShort();
			if (this.varp == 65535) {
				this.varp = -1;
			}
			int defaultId = -1;
			if (opcode == 118) {
				defaultId = stream.readUnsignedShort();
				if (defaultId == 65535) {
					defaultId = -1;
				}
			}
			int size = stream.readUnsignedByte();
			this.transformTo = new int[size + 2];
			for (int i = 0; i <= size; i++) {
				this.transformTo[i] = stream.readUnsignedShort();
				if (this.transformTo[i] == 65535) {
					this.transformTo[i] = -1;
				}
			}
			this.transformTo[size + 1] = defaultId;
		} else if (opcode == 107) {
			this.visible = false;
		} else if (opcode == 109) {
			this.isClickable = false;
		} else if (opcode == 111) {
			this.animateIdle = false;
		} else if (opcode == 113) {
			this.aShort4874 = (short) stream.readUnsignedShort();
			this.aShort4897 = (short) stream.readUnsignedShort();
		} else if (opcode == 114) {
			this.aByte4883 = (byte) stream.readByte();
			this.aByte4899 = (byte) stream.readByte();
		} else if (opcode == 119) {
			this.walkMask = (byte) stream.readByte();
		} else if (opcode == 121) {
			this.modelTranslation = new int[this.modelIds.length][];
			int i_4 = stream.readUnsignedByte();
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				int i_6 = stream.readUnsignedByte();
				int[] translations = this.modelTranslation[i_6] = new int[3];
				translations[0] = stream.readByte();
				translations[1] = stream.readByte();
				translations[2] = stream.readByte();
			}
		} else if (opcode == 123) {
			this.height = stream.readUnsignedShort();
		} else if (opcode == 125) {
			this.respawnDirection = stream.readByte();
		} else if (opcode == 127) {
			this.basId = stream.readUnsignedShort();
		} else if (opcode == 128) {
			this.movementType = MovementType.forId(stream.readUnsignedByte());
		} else if (opcode == 134) {
			this.walkingAnimation = stream.readUnsignedShort();
			if (this.walkingAnimation == 65535) {
				this.walkingAnimation = -1;
			}
			this.rotate180Animation = stream.readUnsignedShort();
			if (this.rotate180Animation == 65535) {
				this.rotate180Animation = -1;
			}
			this.rotate90RightAnimation = stream.readUnsignedShort();
			if (this.rotate90RightAnimation == 65535) {
				this.rotate90RightAnimation = -1;
			}
			this.rotate90LeftAnimation = stream.readUnsignedShort();
			if (this.rotate90LeftAnimation == 65535) {
				this.rotate90LeftAnimation = -1;
			}
			this.specialByte = stream.readUnsignedByte();
		} else if (opcode == 135) {
			this.anInt4875 = stream.readUnsignedByte();
			this.anInt4873 = stream.readUnsignedShort();
		} else if (opcode == 136) {
			this.anInt4854 = stream.readUnsignedByte();
			this.anInt4861 = stream.readUnsignedShort();
		} else if (opcode == 137) {
			this.attackOpCursor = stream.readUnsignedShort();
		} else if (opcode == 138) {
			this.armyIcon = stream.readBigSmart();
		} else if (opcode == 140) {
			this.anInt4909 = stream.readUnsignedByte();
		} else if (opcode == 141) {
			this.aBool4884 = true;
		} else if (opcode == 142) {
			this.mapIcon = stream.readUnsignedShort();
		} else if (opcode == 143) {
			this.aBool4890 = true;
		} else if (opcode >= 150 && opcode < 155) {
			this.membersOptions[opcode - 150] = stream.readString();
		} else if (opcode == 155) {
			this.aByte4868 = (byte) stream.readByte();
			this.aByte4869 = (byte) stream.readByte();
			this.aByte4905 = (byte) stream.readByte();
			this.aByte4871 = (byte) stream.readByte();
		} else if (opcode == 158) {
			this.aByte4916 = 1;
		} else if (opcode == 159) {
			this.aByte4916 = 0;
		} else if (opcode == 160) {
			int i_4 = stream.readUnsignedByte();
			this.quests = new int[i_4];
			for (int i_5 = 0; i_5 < i_4; i_5++) {
				this.quests[i_5] = stream.readUnsignedShort();
			}
		} else if (opcode == 162) {
			this.aBool4872 = true;
		} else if (opcode == 163) {
			this.anInt4917 = stream.readUnsignedByte();
		} else if (opcode == 164) {
			this.anInt4911 = stream.readUnsignedShort();
			this.anInt4919 = stream.readUnsignedShort();
		} else if (opcode == 165) {
			this.anInt4913 = stream.readUnsignedByte();
		} else if (opcode == 168) {
			this.anInt4908 = stream.readUnsignedByte();
		} else if (opcode == 169) {
			this.aBool4920 = false;
		} else if (opcode == 249) {
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
	
	public boolean write(Store store) {
		return store.getIndex(IndexType.NPCS).putFile(ArchiveType.NPCS.archiveId(id), ArchiveType.NPCS.fileId(id), encode());
	}
	
	private final byte[] encode() {
		OutputStream stream = new OutputStream();
		
		if (modelIds != null && modelIds.length > 0) {
			stream.writeByte(1);
			stream.writeByte(modelIds.length);
			for (int i = 0;i < modelIds.length;i++)
				stream.writeBigSmart(modelIds[i]);
		}
		
		if (!name.equals("null")) {
			stream.writeByte(2);
			stream.writeString(name);
		}
		
		if (size != 1) {
			stream.writeByte(12);
			stream.writeByte(size);
		}
		
		for (int i = 0;i < 5;i++) {
			if (options[i] != null) {
				stream.writeByte(30+i);
				stream.writeString(options[i]);
			}
		}
		
		if (originalColors != null && modifiedColors != null) {
			stream.writeByte(40);
			stream.writeByte(originalColors.length);
			for (int i = 0; i < originalColors.length; i++) {
				stream.writeShort(originalColors[i]);
				stream.writeShort(modifiedColors[i]);
			}
		}

		if (originalTextures != null && modifiedTextures != null) {
			stream.writeByte(41);
			stream.writeByte(originalTextures.length);
			for (int i = 0; i < originalTextures.length; i++) {
				stream.writeShort(originalTextures[i]);
				stream.writeShort(modifiedTextures[i]);
			}
		}
		
		if (recolourPalette != null) {
			stream.writeByte(42);
			stream.writeByte(recolourPalette.length);
			for (int i = 0; i < recolourPalette.length; i++)
				stream.writeByte(recolourPalette[i]);
		}

		if (headModels != null && headModels.length > 0) {
			stream.writeByte(60);
			stream.writeByte(headModels.length);
			for (int i = 0;i < headModels.length;i++)
				stream.writeBigSmart(headModels[i]);
		}
		
		if (!drawMapdot) {
			stream.writeByte(93);
		}
		
		if (combatLevel != -1) {
			stream.writeByte(95);
			stream.writeShort(combatLevel);
		}
		
		if (resizeX != 128) {
			stream.writeByte(97);
			stream.writeShort(resizeX);
		}
		
		if (resizeY != 128) {
			stream.writeByte(98);
			stream.writeShort(resizeY);
		}
		
		if (aBool4904) {
			stream.writeByte(99);
		}
		
		if (ambient != 0) {
			stream.writeByte(100);
			stream.writeByte(ambient);
		}
		
		if (contrast != 0) {
			stream.writeByte(101);
			stream.writeByte(contrast);
		}
		
		if (headIcons != 0) {
			stream.writeByte(102);
			stream.writeShort(headIcons);
		}
		
		if (rotation != 32) {
			stream.writeByte(103);
			stream.writeShort(rotation);
		}
		
		if (transformTo != null) {
			//TODO write this properly
		}
		
		if (!visible) {
			stream.writeByte(107);
		}
		
		if (!isClickable) {
			stream.writeByte(109);
		}
		
		if (!animateIdle) {
			stream.writeByte(111);
		}
		
		if (aShort4874 != 0 || aShort4897 != 0) {
			stream.writeByte(113);
			stream.writeShort(aShort4874);
			stream.writeShort(aShort4897);
		}
		
		if (aByte4883 != -96 || aByte4899 != -16) {
			stream.writeByte(114);
			stream.writeShort(aByte4883);
			stream.writeShort(aByte4899);
		}
		
		if (walkMask != 0) {
			stream.writeByte(119);
			stream.writeByte(walkMask);
		}
		
		if (modelTranslation != null) {
			stream.writeByte(121);
			int translationCount = 0;
			for (int i = 0;i < modelTranslation.length;i++) {
				if (modelTranslation[i] != null)
					translationCount++;
			}
			stream.writeByte(translationCount);
			for (int i = 0;i < modelTranslation.length;i++) {
				if (modelTranslation[i] != null) {
					stream.writeByte(i);
					stream.writeByte(modelTranslation[i][0]);
					stream.writeByte(modelTranslation[i][1]);
					stream.writeByte(modelTranslation[i][2]);
				}
			}
		}
		
		if (height != -1) {
			stream.writeByte(123);
			stream.writeShort(height);
		}
		
		if (respawnDirection != 4) {
			stream.writeByte(125);
			stream.writeByte(respawnDirection);
		}
		
		if (basId != -1) {
			stream.writeByte(127);
			stream.writeShort(basId);
		}
		
		if (movementType != null) {
			stream.writeByte(128);
			stream.writeByte(movementType.id);
		}
		
		if (walkingAnimation != -1 || rotate180Animation != -1 || rotate90RightAnimation != -1 || rotate90LeftAnimation != -1 || specialByte != 0) {
			stream.writeByte(134);
			stream.writeShort(walkingAnimation);
			stream.writeShort(rotate180Animation);
			stream.writeShort(rotate90RightAnimation);
			stream.writeShort(rotate90LeftAnimation);
			stream.writeByte(specialByte);
		}
		
		if (anInt4875 != -1 || anInt4873 != -1) {
			stream.writeByte(135);
			stream.writeByte(anInt4875);
			stream.writeShort(anInt4873);
		}
		
		if (anInt4854 != -1 || anInt4861 != -1) {
			stream.writeByte(136);
			stream.writeByte(anInt4854);
			stream.writeShort(anInt4861);
		}
		
		if (attackOpCursor != -1) {
			stream.writeByte(137);
			stream.writeShort(attackOpCursor);
		}
		
		if (armyIcon != -1) {
			stream.writeByte(138);
			stream.writeBigSmart(armyIcon);
		}
		
		if (anInt4909 != 255) {
			stream.writeByte(140);
			stream.writeByte(anInt4909);
		}
		
		if (aBool4884) {
			stream.writeByte(141);
		}
		
		if (mapIcon != -1) {
			stream.writeByte(142);
			stream.writeShort(mapIcon);
		}
		
		if (aBool4890) {
			stream.writeByte(143);
		}
		
		for (int i = 0;i < 5;i++) {
			if (membersOptions[i] != null) {
				stream.writeByte(150+i);
				stream.writeString(membersOptions[i]);
			}
		}
		
		if (aByte4868 != 0 || aByte4869 != 0 || aByte4905 != 0 || aByte4871 != 0) {
			stream.writeByte(155);
			stream.writeByte(aByte4868);
			stream.writeByte(aByte4869);
			stream.writeByte(aByte4905);
			stream.writeByte(aByte4871);
		}
		
		if (aByte4916 != -1 && aByte4916 == 1) {
			stream.writeByte(158);
		}
		
		if (aByte4916 != -1 && aByte4916 == 0) {
			stream.writeByte(159);
		}
		
		if (quests != null && quests.length > 0) {
			stream.writeByte(160);
			stream.writeByte(quests.length);
			for (int i = 0;i < quests.length;i++) {
				stream.writeShort(quests[i]);
			}
		}
		
		if (aBool4872) {
			stream.writeByte(162);
		}
		
		if (anInt4917 != -1) {
			stream.writeByte(163);
			stream.writeByte(anInt4917);
		}
		
		if (anInt4911 != 256 || anInt4919 != 256) {
			stream.writeByte(164);
			stream.writeShort(anInt4911);
			stream.writeShort(anInt4919);
		}
		
		if (anInt4913 != 0) {
			stream.writeByte(165);
			stream.writeByte(anInt4913);
		}
		
		if (anInt4908 != 0) {
			stream.writeByte(168);
			stream.writeByte(anInt4908);
		}
		
		if (!aBool4920) {
			stream.writeByte(169);
		}
		
		if (parameters != null) {
			stream.writeByte(249);
			stream.writeByte(parameters.size());
			for (int key : parameters.keySet()) {
				Object value = parameters.get(key);
				stream.writeByte(value instanceof String ? 1 : 0);
				stream.write24BitInt(key);
				if (value instanceof String) {
					stream.writeString((String) value);
				} else {
					stream.writeInt((Integer) value);
				}
			}
		}
		stream.writeByte(0);
		
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
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

	public static final void clearNPCDefinitions() {
		MAP.clear();
	}
	
	public enum MovementType {
		STATIONARY(-1),
		HALF_WALK(0),
		WALKING(1),
		RUNNING(2);
		
		public int id;
		
		private MovementType(int id) {
			this.id = id;
		}
		
		public static MovementType forId(int id) {
			for (MovementType type : MovementType.values()) {
				if (type.id == id)
					return type;
			}
			return null;
		}
	}

	public NPCDefinitions(int id) {
		this.id = id;
	}
	
	public int getStabAtt() {
		return getBonus(0);
	}
	
	public int getSlashAtt() {
		return getBonus(1);
	}
	
	public int getCrushAtt() {
		return getBonus(2);
	}
	
	public int getMagicAtt() {
		return getBonus(3);
	}
	
	public int getRangeAtt() {
		return getBonus(4);
	}
	
	public int getStabDef() {
		return getBonus(5);
	}
	
	public int getSlashDef() {
		return getBonus(6);
	}
	
	public int getCrushDef() {
		return getBonus(7);
	}
	
	public int getMagicDef() {
		return getBonus(8);
	}
	
	public int getRangeDef() {
		return getBonus(9);
	}
	
	public int getMagicStr() {
		return getBonus(965) / 10;
	}
	
	public int getMeleeStr() {
		return getBonus(641) / 10;
	}
	
	public int getRangeStr() {
		return getBonus(643) / 10;
	}
	
	public int getAttackDelay() {
		int speed = getBonus(14);
		return speed <= 0 ? 4 : speed;
	}
	
	public int getBoBSlots() {
		return getCSValue(379);
	}
	
	public int getSummoningReq() {
		return getCSValue(394);
	}
	
	public int getSummoningDurationMins() {
		return getCSValue(424);
	}
	
	public boolean isBeastOfBurden() {
		return getCSValue(1323) == 1;
	}
	
	public int getCSValue(int key) {
		if (parameters == null)
			return -1;
		if (parameters.get(key) == null || !(parameters.get(id) instanceof Integer))
			return -1;
		return (Integer) parameters.get(key);
	}
	
	public int getBonus(int id) {
		if (parameters == null)
			return 0;
		if (parameters.get(id) == null || !(parameters.get(id) instanceof Integer))
			return 0;
		int bonus = (Integer) parameters.get(id);
		return bonus;
	}
	
	public int getBonus(Bonus bonus) {
		return switch(bonus) {
			case CRUSH_ATT -> getCrushAtt();
			case CRUSH_DEF -> getCrushDef();
			case MAGIC_ATT -> getMagicAtt();
			case MAGIC_DEF -> getMagicDef();
			case MAGIC_STR -> getMagicStr();
			case MELEE_STR -> getMeleeStr();
			case RANGE_ATT -> getRangeAtt();
			case RANGE_DEF -> getRangeDef();
			case RANGE_STR -> getRangeStr();
			case SLASH_ATT -> getSlashAtt();
			case SLASH_DEF -> getSlashDef();
			case STAB_ATT -> getStabAtt();
			case STAB_DEF -> getStabDef();
			default -> 0;
		};
	}

	public boolean isDungNPC() {
		return (id >= 9724 && id <= 11229) || (id >= 11708 && id <= 12187) || (id >= 12436 && id <= 13090) || hasOption("mark");
	}

	public boolean hasOption(String op) {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase(op))
				return true;
		}
		for (String option : membersOptions) {
			if (option != null && option.equalsIgnoreCase(op))
				return true;
		}
		return false;
	}
	
	public String getOption(int op) {
		if (options == null && membersOptions == null)
			return "null";
		if (op >= options.length)
			return "null";
		if (options[op] != null)
			return options[op];
		if (membersOptions[op] != null)
			return membersOptions[op];
		return "null";
	}

	public boolean hasAttackOption() {
		for (String option : options) {
			if (option != null && (option.equalsIgnoreCase("attack") || option.equalsIgnoreCase("destroy")))
				return true;
		}
		for (String option : membersOptions) {
			if (option != null && (option.equalsIgnoreCase("attack") || option.equalsIgnoreCase("destroy")))
				return true;
		}
		return false;
	}

	public Object getParam(int is) {
		if (parameters == null)
			return null;
		return parameters.get(id);
	}
	
	public String getConfigInfoString() {
		String finalString = "";
		String transforms = "\r\n";
		boolean found = false;
		for (int npcId = 0;npcId < Utils.getNPCDefinitionsSize();npcId++) {
			NPCDefinitions defs = getDefs(npcId);
			if (defs.transformTo == null)
				continue;
			for (int i = 0;i < defs.transformTo.length;i++) {
				if (defs.transformTo[i] == id) {
					found = true;
					transforms += "[" + npcId + "("+defs.getName()+")" +":";
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
	
	public String getName() {
		return getName(null);
	}

	public String getName(VarManager vars) {
		int realId = getIdForPlayer(vars);
		if (realId == id)
			return name;
		if (realId == -1)
			return "null";
		return getDefs(realId).name;
	}
	
	public static NPCDefinitions getDefs(int id, VarManager vars) {
		NPCDefinitions defs = getDefs(getDefs(id).getIdForPlayer(vars));
		if (defs == null)
			return getDefs(id);
		return defs;
	}
	
	private static String[] UNDEAD = new String[] {
			"zombie", "skeleton", "banshee", "ankou", "undead", "ghast", "ghost", "crawling hand", "skogre", "zogre", "mummy", "revenant", "shade"
	};

	public boolean isUndead() {
		for(String s : UNDEAD) {
			if(getName() != null && getName().toLowerCase().contains(s))
				return true;
		}
		return false;
	}

	public boolean crawlWalkRender() {
		return usesCrawlWalkBAS;
	}
	
	public int[] getBonuses() {
		return bonuses.clone();
	}
	
	public int[] getStrBonuses() {
		return strBonuses.clone();
	}
}
