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
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.cache.loaders;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public class TextureDefinitions {
	
	private static HashMap<Integer, TextureDefinitions> TEXTURE_DEFINITIONS = new HashMap<>();
	
	public int id;
	public boolean isGroundMesh;
	public boolean isHalfSize;
	public boolean skipTriangles;
	public int brightness;
	public int shadowFactor;
	public int effectId;
	public int effectParam1;
	public int color;
	public int textureSpeedU;
	public int textureSpeedV;
	public boolean aBoolean527;
	public boolean isBrickTile;
	public int useMipmaps;
	public boolean repeatS;
	public boolean repeatT;
	public boolean hdr;
	public int combineMode;
	public int effectParam2;
	public int blendType;
	
	private TextureDefinitions(int id) {
		this.id = id;
	}
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		parseTextureDefs();
		
		for (TextureDefinitions defs : TEXTURE_DEFINITIONS.values()) {
			System.out.println(defs);
		}
	}
	
	public static boolean exists(int id) {
		if (TEXTURE_DEFINITIONS.isEmpty())
			parseTextureDefs();
		return TEXTURE_DEFINITIONS.containsKey(id);
	}
	
	public static void parseTextureDefs() {
		TEXTURE_DEFINITIONS.clear();
		
		byte[] data = Cache.STORE.getIndex(IndexType.TEXTURES).getFile(0, 0);
		InputStream stream = new InputStream(data);
		int len = stream.readUnsignedShort();
		
		for (int i = 0;i < len;i++) {
			if (stream.readUnsignedByte() == 1)
				TEXTURE_DEFINITIONS.put(i, new TextureDefinitions(i));
		}
		
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).isGroundMesh = stream.readUnsignedByte() == 0;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).isHalfSize = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).skipTriangles = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).brightness = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).shadowFactor = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).effectId = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).effectParam1 = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).color = stream.readUnsignedShort();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).textureSpeedU = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).textureSpeedV = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).aBoolean527 = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).isBrickTile = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).useMipmaps = stream.readByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).repeatS = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).repeatT = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).hdr = stream.readUnsignedByte() == 1;
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).combineMode = stream.readUnsignedByte();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).effectParam2 = stream.readInt();
		}
		for (int i = 0;i < len;i++) {
			if (getDefinitions(i) != null)
				getDefinitions(i).blendType = stream.readUnsignedByte();
		}
	}
	
	public static byte[] encode() {
		OutputStream stream = new OutputStream();
		int lastId = getHighestId()+1;
		stream.writeShort(lastId);
		for (int i = 0; i < lastId; i++) {
			stream.writeBoolean(getDefinitions(i) != null);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(!getDefinitions(i).isGroundMesh);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).isHalfSize);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).skipTriangles);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).brightness);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).shadowFactor);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).effectId);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).effectParam1);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeShort(getDefinitions(i).color);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).textureSpeedU);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).textureSpeedV);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).aBoolean527);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).isBrickTile);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).useMipmaps);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).repeatS);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).repeatT);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeBoolean(getDefinitions(i).hdr);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).combineMode);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeInt(getDefinitions(i).effectParam2);
		}
		for (int i = 0; i < lastId; i++) {
			if (getDefinitions(i) != null)
				stream.writeByte(getDefinitions(i).blendType);
		}
		return stream.toByteArray();
	}
	
	public static int getHighestId() {
		if (TEXTURE_DEFINITIONS.isEmpty())
			parseTextureDefs();
		int highest = 0;
		for (TextureDefinitions i : TEXTURE_DEFINITIONS.values())
			if (i.id > highest)
				highest = i.id;
		return highest;
	}
	
	public static TextureDefinitions getDefinitions(int id) {
		if (TEXTURE_DEFINITIONS.isEmpty())
			parseTextureDefs();
		return TEXTURE_DEFINITIONS.get(id);
	}
	
	public static TextureDefinitions getRS3(int id, byte[] stream) {
		if (TEXTURE_DEFINITIONS.isEmpty())
			parseTextureDefs();
		TextureDefinitions defs = new TextureDefinitions(id);
		defs.decodeRS3(new InputStream(stream));
		return defs;
	}
	
	public static void addDef(TextureDefinitions defs) {
		if (TEXTURE_DEFINITIONS.isEmpty())
			parseTextureDefs();
		TEXTURE_DEFINITIONS.put(defs.id, defs);
	}
	
	public static boolean encodeAndReplace() {
		if (TEXTURE_DEFINITIONS.isEmpty())
			parseTextureDefs();
		return Cache.STORE.getIndex(IndexType.TEXTURES).putFile(0, 0, encode());
	}
	
	@SuppressWarnings("unused")
	private void decodeRS3(InputStream buffer) {
		buffer.readUnsignedByte();
		int flag = buffer.readInt();
		if ((flag & 0x1) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x1000) != 0) {
			for (int i = 0;i < 6;i++) {
				buffer.readShort();
				buffer.readUnsignedByte();
			}
		}
		if ((flag & 0x2) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x4) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x8) != 0) {
			buffer.readUnsignedShort();
		}
		if ((flag & 0x10) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x20) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x40) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x80) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x100) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x200) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x400) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		if ((flag & 0x800) != 0) {
			buffer.readUnsignedShort();
			buffer.readUnsignedByte();
		}
		int wrappingFlag = (byte) buffer.readUnsignedByte();
		repeatS = (byte) (wrappingFlag & 0x7) != 0;
		repeatT = (byte) (wrappingFlag >> 3 & 0x7) != 0;
		int settings = buffer.readInt();
		if ((settings & 0x10) != 0) {
			buffer.readFloat();
			buffer.readFloat();
		}
		if (0 != (settings & 0x20))
			buffer.readInt();
		if ((settings & 0x40) != 0)
			buffer.readInt();
		if ((settings & 0x80) != 0)
			buffer.readInt();
		if (0 != (settings & 0x100))
			buffer.readInt();
		if (0 != (settings & 0x200))
			buffer.readInt();
		hdr = buffer.readUnsignedByte() == 1;
		buffer.readUnsignedByte();
		buffer.readUnsignedByte();
		blendType = buffer.readByte();
		if(blendType == 1) {
			int blendMode = buffer.readByte();
		}
		int speedFlag = buffer.readUnsignedByte();
		if (0 != (speedFlag & 0x1)) {
			textureSpeedU = (byte) buffer.readUnsignedByte();
		}
		if (0 != (speedFlag & 0x2)) {
			textureSpeedV = (byte) buffer.readUnsignedByte();
		}
		if (0 != (speedFlag & 0x4)) {
			buffer.readUnsignedByte();
		}
		if (0 != (speedFlag & 0x8)) {
			buffer.readUnsignedByte();
		}
		if (buffer.readUnsignedByte() == 1) {
			effectId = (byte) buffer.readUnsignedByte();
			effectParam1 = (byte) buffer.readUnsignedByte();
			effectParam2 = buffer.readInt();
			combineMode = buffer.readUnsignedByte();
			buffer.readUnsignedByte();
			useMipmaps = (byte) buffer.readUnsignedByte();
			skipTriangles = buffer.readUnsignedByte() == 1;
			isGroundMesh = buffer.readUnsignedByte() == 1;
			brightness = (byte) buffer.readUnsignedByte();
			shadowFactor = (byte) buffer.readUnsignedByte();
			color = (short) buffer.readUnsignedShort();
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
