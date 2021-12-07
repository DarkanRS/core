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
package com.rs.cache.loaders.animations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class AnimationFrameBase {
	
	private static final ConcurrentHashMap<Integer, AnimationFrameBase> FRAME_BASES = new ConcurrentHashMap<Integer, AnimationFrameBase>();

	public int id;
	public int[][] labels;
	public int[] anIntArray7561;
	public int[] transformationTypes;
	public boolean[] aBoolArray7563;
	public int count;
	
	public AnimationFrameBase(int id) {
		this.id = id;
	}
	
	public static AnimationFrameBase getFrame(int frameBaseId) {
		if (FRAME_BASES.get(frameBaseId) != null)
			return FRAME_BASES.get(frameBaseId);
		byte[] frameBaseData = Cache.STORE.getIndex(IndexType.ANIMATION_FRAME_BASES).getFile(frameBaseId, 0);
		if (frameBaseData == null) {
			return null;
		}
		AnimationFrameBase defs = new AnimationFrameBase(frameBaseId);
		defs.decode(new InputStream(frameBaseData));
		FRAME_BASES.put(frameBaseId, defs);
		return defs;
	}
	
	public void decode(InputStream buffer) {
		count = buffer.readUnsignedByte();
		transformationTypes = new int[count];
		labels = new int[count][];
		aBoolArray7563 = new boolean[count];
		anIntArray7561 = new int[count];
		for (int i_0_ = 0; i_0_ < count; i_0_++) {
			transformationTypes[i_0_] = buffer.readUnsignedByte();
			if (transformationTypes[i_0_] == 6)
				transformationTypes[i_0_] = 2;
		}
		for (int i_1_ = 0; i_1_ < count; i_1_++)
			aBoolArray7563[i_1_] = buffer.readUnsignedByte() == 1;
		for (int i_2_ = 0; i_2_ < count; i_2_++)
			anIntArray7561[i_2_] = buffer.readUnsignedShort();
		for (int i_3_ = 0; i_3_ < count; i_3_++)
			labels[i_3_] = new int[buffer.readUnsignedByte()];
		for (int i_4_ = 0; i_4_ < count; i_4_++) {
			for (int i_5_ = 0; (i_5_ < labels[i_4_].length); i_5_++)
				labels[i_4_][i_5_] = buffer.readUnsignedByte();
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
