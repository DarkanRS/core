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

public class Envelope {
	int form;
	int start;
	int end;
	int critical;
	int phaseIndex;
	int step;
	int amplitude;
	int ticks;
	int numPhases = 2;
	int[] phaseDuration = new int[2];
	int[] phasePeak = new int[2];

	final void decode(InputStream buffer) {
		this.form = buffer.readUnsignedByte();
		this.start = buffer.readInt();
		this.end = buffer.readInt();
		this.decodeShape(buffer);
	}

	final void reset() {
		this.critical = 0;
		this.phaseIndex = 0;
		this.step = 0;
		this.amplitude = 0;
		this.ticks = 0;
	}

	Envelope() {
		this.phaseDuration[0] = 0;
		this.phaseDuration[1] = 65535;
		this.phasePeak[0] = 0;
		this.phasePeak[1] = 65535;
	}

	final void decodeShape(InputStream buffer) {
		this.numPhases = buffer.readUnsignedByte();
		this.phaseDuration = new int[this.numPhases];
		this.phasePeak = new int[this.numPhases];

		for (int i = 0; i < this.numPhases; i++) {
			this.phaseDuration[i] = buffer.readUnsignedShort();
			this.phasePeak[i] = buffer.readUnsignedShort();
		}

	}

	final int step(int period) {
		if (this.ticks >= this.critical) {
			this.amplitude = this.phasePeak[this.phaseIndex++] << 15;
			if (this.phaseIndex >= this.numPhases) {
				this.phaseIndex = this.numPhases - 1;
			}

			this.critical = (int) ((double) this.phaseDuration[this.phaseIndex] / 65536.0D * (double) period);
			if (this.critical > this.ticks) {
				this.step = ((this.phasePeak[this.phaseIndex] << 15) - this.amplitude) / (this.critical - this.ticks);
			}
		}

		this.amplitude += this.step;
		++this.ticks;
		return this.amplitude - this.step >> 15;
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
