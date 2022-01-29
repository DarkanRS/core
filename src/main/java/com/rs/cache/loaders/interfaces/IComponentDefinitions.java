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
package com.rs.cache.loaders.interfaces;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class IComponentDefinitions {

	private static IComponentDefinitions[][] COMPONENT_DEFINITIONS;
	private static IFTargetParams GLOBAL_SETTINGS = new IFTargetParams(0, -1);

	@SuppressWarnings("rawtypes")
	public Hashtable aHashTable4823;
	public ComponentType type;
	public String name;
	public int contentType = 0;
	public int basePositionX = 0;
	public int basePositionY = 0;
	public int baseWidth = 0;
	public int baseHeight = 0;
	public byte aspectWidthType = 0;
	public byte aspectHeightType = 0;
	public byte aspectXType = 0;
	public byte aspectYType = 0;
	public int parent = -1;
	public boolean hidden = false;
	public int scrollWidth = 0;
	public int scrollHeight = 0;
	public boolean noClickThrough = false;
	public int spriteId = -1;
	public int angle2d = 0;
	public ModelType modelType = ModelType.RAW_MODEL;
	public int modelId;
	public boolean tiling = false;
	public int fontId = -1;
	public String text = "";
	public int color = 0;
	public boolean alpha = false;
	public int transparency = 0;
	public int borderThickness = 0;
	public int anInt1324 = 0;
	public int anInt1358 = 0;
	public int textHorizontalAli = 0;
	public int textVerticalAli = 0;
	public int lineWidth = 1;
	public boolean hasOrigin;
	public boolean monospaced = true;
	public boolean filled = false;
	public byte[][] aByteArrayArray1366;
	public byte[][] aByteArrayArray1367;
	public int[] anIntArray1395;
	public int[] anIntArray1267;
	public String useOnName = "";
	public boolean vFlip;
	public boolean shadow = false;
	public boolean lineDirection = false;
	public String[] optionNames;
	public boolean usesOrthogonal = false;
	public int multiline = 0;
	public int[] opCursors;
	public boolean hFlip;
	public String opName;
	public boolean aBool1345 = false;
	public boolean aBool1424;
	public int anInt1380;
	public int anInt1381;
	public int anInt1382;
	public String useOptionString = "";
	public int originX = 0;
	public int originY = 0;
	public int spritePitch = 0;
	public int spriteRoll = 0;
	public int spriteYaw = 0;
	public int spriteScale = 100;
	public boolean clickMask = true;
	public int originZ = 0;
	public int animation = -1;
	public int targetOverCursor = -1;
	public int mouseOverCursor = -1;
	public IFTargetParams targetParams = GLOBAL_SETTINGS;
	public int aspectWidth = 0;
	public int targetLeaveCursor = -1;
	public Object[] onLoadScript;
	public Object[] onMouseHoverScript;
	public Object[] onMouseLeaveScript;
	public Object[] anObjectArray1396;
	public Object[] anObjectArray1400;
	public Object[] anObjectArray1397;
	public Object[] mouseLeaveScript;
	public Object[] anObjectArray1387;
	public Object[] anObjectArray1409;
	public Object[] params;
	public int aspectHeight = 0;
	public Object[] anObjectArray1393;
	public Object[] popupScript;
	public Object[] anObjectArray1386;
	public Object[] anObjectArray1319;
	public Object[] anObjectArray1302;
	public Object[] anObjectArray1389;
	public Object[] anObjectArray1451;
	public Object[] anObjectArray1394;
	public Object[] anObjectArray1412;
	public Object[] anObjectArray1403;
	public Object[] anObjectArray1405;
	public int[] varps;
	public int[] mouseLeaveArrayParam;
	public int[] anIntArray1402;
	public int[] anIntArray1315;
	public int[] anIntArray1406;
	public Object[] anObjectArray1413;
	public Object[] anObjectArray1292;
	public Object[] anObjectArray1415;
	public Object[] anObjectArray1416;
	public Object[] anObjectArray1383;
	public Object[] anObjectArray1419;
	public Object[] anObjectArray1361;
	public Object[] anObjectArray1421;
	public Object[] anObjectArray1346;
	public Object[] anObjectArray1353;
	public Object[] anObjectArray1271;
	public boolean usesScripts;
	public int uid = -1;
	public int anInt1288 = -1;
	public int x = 0;
	public int y = 0;
	public int width = 0;
	public int height = 0;
	public int anInt1289 = 1;
	public int anInt1375 = 1;
	public int scrollX = 0;
	public int scrollY = 0;
	public int anInt1339 = -1;
	public int anInt1293 = 0;
	public int anInt1334 = 0;
	public int anInt1335 = 2;
	public int interfaceId = -1;
	public int componentId = -1;
	
	public static boolean checkForScripts(int scriptId, Object[]... arrs) {
		for (Object[] arr : arrs) {
			if (arr == null)
				continue;
			for (int i = 0;i < arr.length;i++)
				try {
					if (arr[i] != null && (Integer) arr[i] == scriptId)
						return true;
				} catch (ClassCastException e) {
					
				}
		}
		return false;
	}
	
	public boolean usesScript(int scriptId) {
		if (checkForScripts(scriptId, onLoadScript, onMouseHoverScript, onMouseLeaveScript, anObjectArray1396, anObjectArray1400, anObjectArray1397, mouseLeaveScript, anObjectArray1387, anObjectArray1409,
				params, anObjectArray1393, popupScript, anObjectArray1386, anObjectArray1319, anObjectArray1302, anObjectArray1389, anObjectArray1451, anObjectArray1394, anObjectArray1412, anObjectArray1403,
				anObjectArray1405, anObjectArray1413, anObjectArray1292, anObjectArray1415, anObjectArray1416, anObjectArray1383, anObjectArray1419, anObjectArray1361, anObjectArray1421, 
				anObjectArray1346, anObjectArray1353, anObjectArray1271))
			return true;
		return false;
	}

	public static void main(String[] args) throws IOException {
		//Cache.init();
		COMPONENT_DEFINITIONS = new IComponentDefinitions[Utils.getInterfaceDefinitionsSize()][];

		int scriptId = 5513;
		
		for (int id = 0;id < COMPONENT_DEFINITIONS.length;id++) {
			IComponentDefinitions[] defs = getInterface(id);
			for (int comp = 0;comp < defs.length;comp++) {
				if (defs[comp].usesScript(scriptId))
					System.out.println("Interface: " + id + ", " + comp);
			}
		}
//		IComponentDefinitions[] defs = getInterface(499);
//		for (IComponentDefinitions def : defs) {
//			//if (def.baseWidth != 0)
//			//	System.out.println(def.uid + " - " + def.baseWidth);
//				System.out.println(def);
//		}	
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		IComponentDefinitions def = new IComponentDefinitions();

		result.append(this.getClass().getName());
		result.append(" {");
		result.append(newLine);

		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			try {
				Object f1 = Utils.getFieldValue(this, field);
				Object f2 = Utils.getFieldValue(def, field);
				if (f1 == f2 || f1.equals(f2))
					continue;
				result.append("  ");
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

	public static IComponentDefinitions getInterfaceComponent(int id, int component) {
		IComponentDefinitions[] inter = getInterface(id);
		if (inter == null || component >= inter.length)
			return null;
		return inter[component];
	}

	public static IComponentDefinitions[] getInterface(int id) {
		if (COMPONENT_DEFINITIONS == null)
			COMPONENT_DEFINITIONS = new IComponentDefinitions[Utils.getInterfaceDefinitionsSize()][];
		if (id >= COMPONENT_DEFINITIONS.length)
			return null;
		if (COMPONENT_DEFINITIONS[id] == null) {
			COMPONENT_DEFINITIONS[id] = new IComponentDefinitions[Utils.getInterfaceDefinitionsComponentsSize(id)];
			for (int i = 0; i < COMPONENT_DEFINITIONS[id].length; i++) {
				byte[] data = Cache.STORE.getIndex(IndexType.INTERFACES).getFile(id, i);
				if (data != null) {
					IComponentDefinitions defs = COMPONENT_DEFINITIONS[id][i] = new IComponentDefinitions();
					defs.uid = i + (id << 16);
					defs.interfaceId = id;
					defs.componentId = i;
					if (data[0] != -1) {
						throw new IllegalStateException("if1");
					}
					defs.decode(new InputStream(data));
				}
			}
		}
		return COMPONENT_DEFINITIONS[id];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final void decode(InputStream stream) {
		int i_3 = stream.readUnsignedByte();
		if (i_3 == 255) {
			i_3 = -1;
		}
		int typeId = stream.readUnsignedByte();
		if ((typeId & 0x80) != 0) {
			typeId &= 0x7f;
			this.name = stream.readString();
		}
		this.type = ComponentType.forId(typeId);
		this.contentType = stream.readUnsignedShort();
		this.basePositionX = stream.readShort();
		this.basePositionY = stream.readShort();
		this.baseWidth = stream.readUnsignedShort();
		this.baseHeight = stream.readUnsignedShort();
		this.aspectWidthType = (byte) stream.readByte();
		this.aspectHeightType = (byte) stream.readByte();
		this.aspectXType = (byte) stream.readByte();
		this.aspectYType = (byte) stream.readByte();
		this.parent = stream.readUnsignedShort();
		if (this.parent == 65535) {
			this.parent = -1;
		} else {
			this.parent += this.uid & ~0xffff;
		}
		int i_4 = stream.readUnsignedByte();
		this.hidden = (i_4 & 0x1) != 0;
		if (i_3 >= 0) {
			this.noClickThrough = (i_4 & 0x2) != 0;
		}
		if (this.type == ComponentType.CONTAINER) {
			this.scrollWidth = stream.readUnsignedShort();
			this.scrollHeight = stream.readUnsignedShort();
			if (i_3 < 0) {
				this.noClickThrough = stream.readUnsignedByte() == 1;
			}
		}
		if (this.type == ComponentType.SPRITE) {
			this.spriteId = stream.readInt();
			this.angle2d = stream.readUnsignedShort();
			int flag2 = stream.readUnsignedByte();
			this.tiling = (flag2 & 0x1) != 0;
			this.alpha = (flag2 & 0x2) != 0;
			this.transparency = stream.readUnsignedByte();
			this.borderThickness = stream.readUnsignedByte();
			this.anInt1324 = stream.readInt();
			this.vFlip = stream.readUnsignedByte() == 1;
			this.hFlip = stream.readUnsignedByte() == 1;
			this.color = stream.readInt();
			if (i_3 >= 3) {
				this.clickMask = stream.readUnsignedByte() == 1;
			}
		}
		if (this.type == ComponentType.MODEL) {
			this.modelType = ModelType.RAW_MODEL;
			this.modelId = stream.readBigSmart();
			int flag2 = stream.readUnsignedByte();
			boolean bool_6 = (flag2 & 0x1) == 1;
			this.hasOrigin = (flag2 & 0x2) == 2;
			this.usesOrthogonal = (flag2 & 0x4) == 4;
			this.aBool1345 = (flag2 & 0x8) == 8;
			if (bool_6) {
				this.originX = stream.readShort();
				this.originY = stream.readShort();
				this.spritePitch = stream.readUnsignedShort();
				this.spriteRoll = stream.readUnsignedShort();
				this.spriteYaw = stream.readUnsignedShort();
				this.spriteScale = stream.readUnsignedShort();
			} else if (this.hasOrigin) {
				this.originX = stream.readShort();
				this.originY = stream.readShort();
				this.originZ = stream.readShort();
				this.spritePitch = stream.readUnsignedShort();
				this.spriteRoll = stream.readUnsignedShort();
				this.spriteYaw = stream.readUnsignedShort();
				this.spriteScale = stream.readShort();
			}
			this.animation = stream.readBigSmart();
			if (this.aspectWidthType != 0) {
				this.aspectWidth = stream.readUnsignedShort();
			}
			if (this.aspectHeightType != 0) {
				this.aspectHeight = stream.readUnsignedShort();
			}
		}
		if (this.type == ComponentType.TEXT) {
			this.fontId = stream.readBigSmart();
			if (i_3 >= 2) {
				this.monospaced = stream.readUnsignedByte() == 1;
			}
			this.text = stream.readString();
			if (this.text.toLowerCase().contains("runescape")) {
				this.text = this.text.replace("runescape", "Darkan");
				this.text = this.text.replace("RuneScape", "Darkan");
				this.text = this.text.replace("Runescape", "Darkan");
			}
			this.anInt1358 = stream.readUnsignedByte();
			this.textHorizontalAli = stream.readUnsignedByte();
			this.textVerticalAli = stream.readUnsignedByte();
			this.shadow = stream.readUnsignedByte() == 1;
			this.color = stream.readInt();
			this.transparency = stream.readUnsignedByte();
			if (i_3 >= 0) {
				this.multiline = stream.readUnsignedByte();
			}
		}
		if (this.type == ComponentType.FIGURE) {
			this.color = stream.readInt();
			this.filled = stream.readUnsignedByte() == 1;
			this.transparency = stream.readUnsignedByte();
		}
		if (this.type == ComponentType.LINE) {
			this.lineWidth = stream.readUnsignedByte();
			this.color = stream.readInt();
			this.lineDirection = stream.readUnsignedByte() == 1;
		}
		int optionMask = stream.read24BitUnsignedInteger();
		int i_16 = stream.readUnsignedByte();
		int i_7;
		if (i_16 != 0) {
			this.aByteArrayArray1366 = new byte[11][];
			this.aByteArrayArray1367 = new byte[11][];
			this.anIntArray1395 = new int[11];
			for (this.anIntArray1267 = new int[11]; i_16 != 0; i_16 = stream.readUnsignedByte()) {
				i_7 = (i_16 >> 4) - 1;
				i_16 = i_16 << 8 | stream.readUnsignedByte();
				i_16 &= 0xfff;
				if (i_16 == 4095) {
					i_16 = -1;
				}
				byte b_8 = (byte) stream.readByte();
				if (b_8 != 0) {
					this.aBool1424 = true;
				}
				byte b_9 = (byte) stream.readByte();
				this.anIntArray1395[i_7] = i_16;
				this.aByteArrayArray1366[i_7] = new byte[] { b_8 };
				this.aByteArrayArray1367[i_7] = new byte[] { b_9 };
			}
		}
		this.useOnName = stream.readString();
		i_7 = stream.readUnsignedByte();
		int i_17 = i_7 & 0xf;
		int i_18 = i_7 >> 4;
		int i_10;
		if (i_17 > 0) {
			this.optionNames = new String[i_17];
			for (i_10 = 0; i_10 < i_17; i_10++) {
				this.optionNames[i_10] = stream.readString();
			}
		}
		int i_11;
		if (i_18 > 0) {
			i_10 = stream.readUnsignedByte();
			this.opCursors = new int[i_10 + 1];
			for (i_11 = 0; i_11 < this.opCursors.length; i_11++) {
				this.opCursors[i_11] = -1;
			}
			this.opCursors[i_10] = stream.readUnsignedShort();
		}
		if (i_18 > 1) {
			i_10 = stream.readUnsignedByte();
			this.opCursors[i_10] = stream.readUnsignedShort();
		}
		this.opName = stream.readString();
		if (this.opName.equals("")) {
			this.opName = null;
		}
		this.anInt1380 = stream.readUnsignedByte();
		this.anInt1381 = stream.readUnsignedByte();
		this.anInt1382 = stream.readUnsignedByte();
		this.useOptionString = stream.readString();
		i_10 = -1;
		if (IFTargetParams.getUseOptionFlags(optionMask) != 0) {
			i_10 = stream.readUnsignedShort();
			if (i_10 == 65535) {
				i_10 = -1;
			}
			this.targetOverCursor = stream.readUnsignedShort();
			if (this.targetOverCursor == 65535) {
				this.targetOverCursor = -1;
			}
			this.targetLeaveCursor = stream.readUnsignedShort();
			if (this.targetLeaveCursor == 65535) {
				this.targetLeaveCursor = -1;
			}
		}
		if (i_3 >= 0) {
			this.mouseOverCursor = stream.readUnsignedShort();
			if (this.mouseOverCursor == 65535) {
				this.mouseOverCursor = -1;
			}
		}
		this.targetParams = new IFTargetParams(optionMask, i_10);
		if (i_3 >= 0) {
			if (this.aHashTable4823 == null)
				this.aHashTable4823 = new Hashtable();
			i_11 = stream.readUnsignedByte();
			int i_12;
			int i_13;
			int i_14;
			for (i_12 = 0; i_12 < i_11; i_12++) {
				i_13 = stream.read24BitUnsignedInteger();
				i_14 = stream.readInt();
				this.aHashTable4823.put(i_14, (long) i_13);
			}
			i_12 = stream.readUnsignedByte();
			for (i_13 = 0; i_13 < i_12; i_13++) {
				i_14 = stream.read24BitUnsignedInteger();
				String string_15 = stream.readGJString();
				this.aHashTable4823.put(string_15, (long) i_14);
			}
		}
		this.onLoadScript = this.decodeScript(stream);
		this.onMouseHoverScript = this.decodeScript(stream);
		this.onMouseLeaveScript = this.decodeScript(stream);
		this.anObjectArray1396 = this.decodeScript(stream);
		this.anObjectArray1400 = this.decodeScript(stream);
		this.anObjectArray1397 = this.decodeScript(stream);
		this.mouseLeaveScript = this.decodeScript(stream);
		this.anObjectArray1387 = this.decodeScript(stream);
		this.anObjectArray1409 = this.decodeScript(stream);
		this.params = this.decodeScript(stream);
		if (i_3 >= 0) {
			this.anObjectArray1393 = this.decodeScript(stream);
		}
		this.popupScript = this.decodeScript(stream);
		this.anObjectArray1386 = this.decodeScript(stream);
		this.anObjectArray1319 = this.decodeScript(stream);
		this.anObjectArray1302 = this.decodeScript(stream);
		this.anObjectArray1389 = this.decodeScript(stream);
		this.anObjectArray1451 = this.decodeScript(stream);
		this.anObjectArray1394 = this.decodeScript(stream);
		this.anObjectArray1412 = this.decodeScript(stream);
		this.anObjectArray1403 = this.decodeScript(stream);
		this.anObjectArray1405 = this.decodeScript(stream);
		this.varps = this.method4150(stream);
		this.mouseLeaveArrayParam = this.method4150(stream);
		this.anIntArray1402 = this.method4150(stream);
		this.anIntArray1315 = this.method4150(stream);
		this.anIntArray1406 = this.method4150(stream);
	}

	private final Object[] decodeScript(InputStream buffer) {
		int i_29_ = buffer.readUnsignedByte();
		if (0 == i_29_)
			return null;
		Object[] objects = new Object[i_29_];
		for (int i_30_ = 0; i_30_ < i_29_; i_30_++) {
			int i_31_ = buffer.readUnsignedByte();
			if (i_31_ == 0)
				objects[i_30_] = buffer.readInt();
			else if (i_31_ == 1)
				objects[i_30_] = buffer.readString();
		}
		usesScripts = true;
		return objects;
	}

	private final int[] method4150(InputStream buffer) {
		int i = buffer.readUnsignedByte();
		if (i == 0) {
			return null;
		}
		int[] is = new int[i];
		for (int i_60_ = 0; i_60_ < i; i_60_++)
			is[i_60_] = buffer.readInt();
		return is;
	}

	final int method14502(int i) {
		return i >> 11 & 0x7f;
	}
}
