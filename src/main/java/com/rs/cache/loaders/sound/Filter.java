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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class Filter {

	static float[][] coefficient_float = new float[2][8];
	static int[][] coefficient_int = new int[2][8];
	int[] numPairs = new int[2];
	int[][][] pairPhase = new int[2][2][4];
	int[][][] pairMagnitude = new int[2][2][4];
	int[] unity = new int[2];
	static float invUnity_float;
	static int invUnity_int;

	float adaptMagnitude(int dir, int k, float t) {
		float alpha = (float) this.pairMagnitude[dir][0][k] + t * (float) (this.pairMagnitude[dir][1][k] - this.pairMagnitude[dir][0][k]);
		alpha *= 0.0015258789F;
		return 1.0F - (float) Math.pow(10.0D, (double) (-alpha / 20.0F));
	}

	static float normalize(float f) {
		float _f = 32.703197F * (float) Math.pow(2.0D, (double) f);
		return _f * 3.1415927F / 11025.0F;
	}

	float adaptPhase(int dir, int i, float t) {
		float _f = (float) this.pairPhase[dir][0][i] + t * (float) (this.pairPhase[dir][1][i] - this.pairPhase[dir][0][i]);
		_f *= 1.2207031E-4F;
		return normalize(_f);
	}

	final void decode(InputStream buffer, Envelope envelope) {
		int count = buffer.readUnsignedByte();
		this.numPairs[0] = count >> 4;
		this.numPairs[1] = count & 0xf;
		if (count != 0) {
			this.unity[0] = buffer.readUnsignedShort();
			this.unity[1] = buffer.readUnsignedShort();
			int migrated = buffer.readUnsignedByte();

			for (int dir = 0; dir < 2; dir++) {
				for (int term = 0; term < this.numPairs[dir]; term++) {
					this.pairPhase[dir][0][term] = buffer.readUnsignedShort();
					this.pairMagnitude[dir][0][term] = buffer.readUnsignedShort();
				}
			}

			for (int dir = 0; dir < 2; dir++) {
				for (int phase = 0; phase < this.numPairs[dir]; phase++) {
					if ((migrated & 1 << dir * 4 << phase) != 0) {
						this.pairPhase[dir][1][phase] = buffer.readUnsignedShort();
						this.pairMagnitude[dir][1][phase] = buffer.readUnsignedShort();
					} else {
						this.pairPhase[dir][1][phase] = this.pairPhase[dir][0][phase];
						this.pairMagnitude[dir][1][phase] = this.pairMagnitude[dir][0][phase];
					}
				}
			}

			if (migrated != 0 || this.unity[1] != this.unity[0]) {
				envelope.decodeShape(buffer);
			}
		} else {
			int[] placeholder = this.unity;
			this.unity[1] = 0;
			placeholder[0] = 0;
		}

	}

	int compute(int dir, float t) {
		if (dir == 0) {
			float a0 = (float) this.unity[0] + (float) (this.unity[1] - this.unity[0]) * t;
			a0 *= 0.0030517578F;
			invUnity_float = (float) Math.pow(0.1D, (double) (a0 / 20.0F));
			invUnity_int = (int) (invUnity_float * 65536.0F);
		}

		if (this.numPairs[dir] == 0) {
			return 0;
		} else {
			float f_3 = this.adaptMagnitude(dir, 0, t);
			coefficient_float[dir][0] = -2.0F * f_3 * (float) Math.cos((double) this.adaptPhase(dir, 0, t));
			coefficient_float[dir][1] = f_3 * f_3;

			int i_4;
			for (i_4 = 1; i_4 < this.numPairs[dir]; i_4++) {
				f_3 = this.adaptMagnitude(dir, i_4, t);
				float f_5 = -2.0F * f_3 * (float) Math.cos((double) this.adaptPhase(dir, i_4, t));
				float f_6 = f_3 * f_3;
				coefficient_float[dir][i_4 * 2 + 1] = coefficient_float[dir][i_4 * 2 - 1] * f_6;
				coefficient_float[dir][i_4 * 2] = coefficient_float[dir][i_4 * 2 - 1] * f_5 + coefficient_float[dir][i_4 * 2 - 2] * f_6;

				for (int i_7 = i_4 * 2 - 1; i_7 >= 2; --i_7) {
					coefficient_float[dir][i_7] += coefficient_float[dir][i_7 - 1] * f_5 + coefficient_float[dir][i_7 - 2] * f_6;
				}

				coefficient_float[dir][1] += coefficient_float[dir][0] * f_5 + f_6;
				coefficient_float[dir][0] += f_5;
			}

			if (dir == 0) {
				for (i_4 = 0; i_4 < this.numPairs[0] * 2; i_4++) {
					coefficient_float[0][i_4] *= invUnity_float;
				}
			}

			for (i_4 = 0; i_4 < this.numPairs[dir] * 2; i_4++) {
				coefficient_int[dir][i_4] = (int) (coefficient_float[dir][i_4] * 65536.0F);
			}

			return this.numPairs[dir] * 2;
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
