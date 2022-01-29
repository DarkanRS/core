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
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public class ParticleProducerDefinitions {
	
	private static final ConcurrentHashMap<Integer, ParticleProducerDefinitions> PARTICLE_DEFS = new ConcurrentHashMap<Integer, ParticleProducerDefinitions>();
	
	public int id;
	public int[] anIntArray562;
	public short minimumAngleH;
	public short maximumAngleH;
	public short minimumAngleV;
	public short maximumAngleV;
	public int minimumSpeed;
	public int maximumSpeed;
	public int anInt542 = 0;
	public int anInt569;
	public int maximumSize;
	public int minimumSize;
	public int minimumStartColor;
	public int maximumStartColor;
	public int minimumLifetime;
	public int maximumLifetime;
	public int minimumParticleRate;
	public int maximumParticleRate;
	public int[] anIntArray559;
	public int[] anIntArray561;
	public int anInt591 = -2;
	public int anInt600 = -2;
	public int anInt557 = 0;
	public int textureId = -1;
	public int anInt573 = -1;
	public int fadeColor;
	public boolean activeFirst = true;
	public int anInt537 = -1;
	public int lifetime = -1;
	public int minimumSetting = 0;
	public int colorFading = 100;
	public boolean periodic = true;
	public int alphaFading = 100;
	public int endSpeed = -1;
	public int speedChange = 100;
	public boolean uniformColorVariance = true;
	public int[] anIntArray582;
	public boolean aBool572 = true;
	public int endSize = -1;
	public int sizeChange = 100;
	public boolean aBool574 = false;
	public boolean aBool534 = true;
	public boolean aBool576 = false;
	public boolean aBool541 = true;
	public boolean opcode5 = false;
	public boolean opcode31 = false;
	
	/*
	 * Initialized variables
	 */
	public boolean aBool578 = false;
	public int anInt565;
	public int anInt566;
	public int anInt581;
	public int anInt551;
	public int anInt599;
	public int anInt584;
	public int anInt585;
	public int anInt586;
	public int anInt587;
	public int anInt588;
	public int anInt575;
	public int anInt590;
	public int colorFadeStart;
	public int alphaFadeStart;
	public int fadeRedStep;
	public int fadeGreenStep;
	public int fadeBlueStep;
	public int startSpeedChange;
	public int fadeAlphaStep;
	public int speedStep;
	public int startSizeChange;
	public int sizeChangeStep;

	private int opcode2;

	private int opcode29;
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		
//		for (int i = 0;i < Cache.STORE.getIndex(IndexType.PARTICLES).getLastFileId(0);i++) {
//			ParticleProducerDefinitions defs = getDefs(i);
//			System.out.println(defs);
//		}
		System.out.println(getDefs(729));
	}
	
	public static final ParticleProducerDefinitions getDefs(int id) {
		ParticleProducerDefinitions defs = PARTICLE_DEFS.get(id);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndex(IndexType.PARTICLES).getFile(0, id);
		defs = new ParticleProducerDefinitions();
		defs.id = id;
		if (data != null)
			defs.readValueLoop(new InputStream(data), false);
		//defs.init();
		PARTICLE_DEFS.put(id, defs);
		return defs;
	}
	
	public void decode(byte[] data, boolean rs3) {
		readValueLoop(new InputStream(data), rs3);
	}

	private void readValueLoop(InputStream stream, boolean rs3) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode, rs3);
		}
	}

	private void readValues(InputStream buffer, int opcode, boolean rs3) {
		if (opcode == 1) {
			this.minimumAngleH = (short) buffer.readUnsignedShort();
			this.maximumAngleH = (short) buffer.readUnsignedShort();
			this.minimumAngleV = (short) buffer.readUnsignedShort();
			this.maximumAngleV = (short) buffer.readUnsignedShort();
		} else if (opcode == 2) {
			this.opcode2 = buffer.readUnsignedByte();
		} else if (opcode == 3) {
			this.minimumSpeed = buffer.readInt();
			this.maximumSpeed = buffer.readInt();
		} else if (opcode == 4) {
			this.anInt542 = buffer.readUnsignedByte();
			this.anInt569 = buffer.readByte();
		} else if (opcode == 5) {
			this.opcode5 = true;
			this.minimumSize = this.maximumSize = buffer.readUnsignedShort();
		} else if (opcode == 6) {
			this.minimumStartColor = buffer.readInt();
			this.maximumStartColor = buffer.readInt();
		} else if (opcode == 7) {
			this.minimumLifetime = buffer.readUnsignedShort();
			this.maximumLifetime = buffer.readUnsignedShort();
		} else if (opcode == 8) {
			this.minimumParticleRate = buffer.readUnsignedShort();
			this.maximumParticleRate = buffer.readUnsignedShort();
		} else {
			int i_5;
			int count;
			if (opcode == 9) {
				count = buffer.readUnsignedByte();
				this.anIntArray559 = new int[count];

				for (i_5 = 0; i_5 < count; i_5++) {
					this.anIntArray559[i_5] = buffer.readUnsignedShort();
				}
			} else if (opcode == 10) {
				count = buffer.readUnsignedByte();
				this.anIntArray561 = new int[count];

				for (i_5 = 0; i_5 < count; i_5++) {
					this.anIntArray561[i_5] = buffer.readUnsignedShort();
				}
			} else if (opcode == 12) {
				this.anInt591 = buffer.readByte();
			} else if (opcode == 13) {
				this.anInt600 = buffer.readByte();
			} else if (opcode == 14) {
				this.anInt557 = buffer.readUnsignedShort();
			} else if (opcode == 15) {
				this.textureId = buffer.readUnsignedShort();
			} else if (opcode == 16) {
				this.activeFirst = buffer.readUnsignedByte() == 1;
				this.anInt537 = buffer.readUnsignedShort();
				this.lifetime = buffer.readUnsignedShort();
				this.periodic = buffer.readUnsignedByte() == 1;
			} else if (opcode == 17) {
				this.anInt573 = buffer.readUnsignedShort();
			} else if (opcode == 18) {
				this.fadeColor = buffer.readInt();
			} else if (opcode == 19) {
				this.minimumSetting = buffer.readUnsignedByte();
			} else if (opcode == 20) {
				this.colorFading = buffer.readUnsignedByte();
			} else if (opcode == 21) {
				this.alphaFading = buffer.readUnsignedByte();
			} else if (opcode == 22) {
				this.endSpeed = buffer.readInt();
			} else if (opcode == 23) {
				this.speedChange = buffer.readUnsignedByte();
			} else if (opcode == 24) {
				this.uniformColorVariance = false;
			} else if (opcode == 25) {
				count = buffer.readUnsignedByte();
				this.anIntArray582 = new int[count];

				for (i_5 = 0; i_5 < count; i_5++) {
					this.anIntArray582[i_5] = buffer.readUnsignedShort();
				}
			} else if (opcode == 26) {
				this.aBool572 = false;
			} else if (opcode == 27) {
				this.endSize = buffer.readUnsignedShort();
			} else if (opcode == 28) {
				this.sizeChange = buffer.readUnsignedByte();
			} else if (opcode == 29) {
				if (rs3) {
					if (buffer.readUnsignedByte() == 0) {
						buffer.readShort();
					} else {
						buffer.readShort();
						buffer.readShort() ;
					}
				} else
					this.opcode29 = buffer.readShort();
			} else if (opcode == 30) {
				this.aBool574 = true;
			} else if (opcode == 31) {
				this.opcode31 = true;
				this.minimumSize = buffer.readUnsignedShort();
				this.maximumSize = buffer.readUnsignedShort();
			} else if (opcode == 32) {
				this.aBool534 = false;
			} else if (opcode == 33) {
				this.aBool576 = true;
			} else if (opcode == 34) {
				this.aBool541 = false;
			} else if (opcode == 35) {
				if (buffer.readUnsignedByte() == 0) {
					buffer.readShort();
				} else {
					buffer.readShort();
					buffer.readShort();
					buffer.readUnsignedByte();
				}
			}
		}
	}
	
	public byte[] encode() {
		OutputStream stream = new OutputStream();
		if (minimumAngleH != 0 || maximumAngleH != 0 || minimumAngleV != 0 || maximumAngleV != 0) {
			stream.writeByte(1);
			stream.writeShort(minimumAngleH);
			stream.writeShort(maximumAngleH);
			stream.writeShort(minimumAngleV);
			stream.writeShort(maximumAngleV);
		}
		if (opcode2 != 0) {
			stream.writeByte(2);
			stream.writeByte(opcode2);
		}
		if (minimumSpeed != 0 || maximumSpeed != 0) {
			stream.writeByte(3);
			stream.writeInt(minimumSpeed);
			stream.writeInt(maximumSpeed);
		}
		if (anInt542 != 0 || anInt569 != 0) {
			stream.writeByte(4);
			stream.writeByte(anInt542);
			stream.writeByte(anInt569);
		}
		if (opcode5) {
			stream.writeByte(5);
			stream.writeShort(minimumSize);
		}
		if (minimumStartColor != 0 || maximumStartColor != 0) {
			stream.writeByte(6);
			stream.writeInt(minimumStartColor);
			stream.writeInt(maximumStartColor);
		}
		if (minimumLifetime != 0 || maximumLifetime != 0) {
			stream.writeByte(7);
			stream.writeShort(minimumLifetime);
			stream.writeShort(maximumLifetime);
		}
		if (minimumParticleRate != 0 || maximumParticleRate != 0) {
			stream.writeByte(8);
			stream.writeShort(minimumParticleRate);
			stream.writeShort(maximumParticleRate);
		}
		if (anIntArray559 != null) {
			stream.writeByte(9);
			stream.writeByte(anIntArray559.length);
			for (int i = 0;i < anIntArray559.length;i++) {
				stream.writeShort(anIntArray559[i]);
			}
		}
		if (anIntArray561 != null) {
			stream.writeByte(10);
			stream.writeByte(anIntArray561.length);
			for (int i = 0;i < anIntArray561.length;i++) {
				stream.writeShort(anIntArray561[i]);
			}
		}
		if (anInt591 != -2) {
			stream.writeByte(12);
			stream.writeByte(anInt591);
		}
		if (anInt600 != -2) {
			stream.writeByte(13);
			stream.writeByte(anInt600);
		}
		if (anInt557 != 0) {
			stream.writeByte(14);
			stream.writeShort(anInt557);
		}
		if (textureId != -1) {
			stream.writeByte(15);
			stream.writeShort(textureId);
		}
		if (!activeFirst || anInt537 != -1 || lifetime != -1 || !periodic) {
			stream.writeByte(16);
			stream.writeBoolean(activeFirst);
			stream.writeShort(anInt537);
			stream.writeShort(lifetime);
			stream.writeBoolean(periodic);
		}
		if (anInt573 != -1) {
			stream.writeByte(17);
			stream.writeShort(anInt573);
		}
		if (fadeColor != 0) {
			stream.writeByte(18);
			stream.writeInt(fadeColor);
		}
		if (minimumSetting != 0) {
			stream.writeByte(19);
			stream.writeByte(minimumSetting);
		}
		if (colorFading != 100) {
			stream.writeByte(20);
			stream.writeByte(colorFading);
		}
		if (alphaFading != 100) {
			stream.writeByte(21);
			stream.writeByte(alphaFading);
		}
		if (endSpeed != -1) {
			stream.writeByte(22);
			stream.writeInt(endSpeed);
		}
		if (speedChange != 100) {
			stream.writeByte(23);
			stream.writeByte(speedChange);
		}
		if (!uniformColorVariance) {
			stream.writeByte(24);
		}
		if (anIntArray582 != null) {
			stream.writeByte(25);
			stream.writeByte(anIntArray582.length);
			for (int i = 0;i < anIntArray582.length;i++) {
				stream.writeShort(anIntArray582[i]);
			}
		}
		if (!aBool572) {
			stream.writeByte(26);
		}
		if (endSize != -1) {
			stream.writeByte(27);
			stream.writeShort(endSize);
		}
		if (sizeChange != 100) {
			stream.writeByte(28);
			stream.writeByte(sizeChange);
		}
		if (opcode29 != 0) {
			stream.writeByte(29);
			stream.writeShort(opcode29);
		}
		if (aBool574) {
			stream.writeByte(30);
		}
		if (opcode31) {
			stream.writeByte(31);
			stream.writeShort(minimumSize);
			stream.writeShort(maximumSize);
		}
		if (!aBool534) {
			stream.writeByte(32);
		}
		if (aBool576) {
			stream.writeByte(33);
		}
		if (!aBool541) {
			stream.writeByte(34);
		}
		stream.writeByte(0);
		return stream.toByteArray();
	}
	
	public boolean write(Store store) {
		return store.getIndex(IndexType.PARTICLES).putFile(0, id, encode());
	}
	
	void init() {
		if (this.anInt591 > -2 || this.anInt600 > -2) {
			this.aBool578 = true;
		}

		this.anInt565 = this.minimumStartColor >> 16 & 0xff;
		this.anInt566 = this.maximumStartColor >> 16 & 0xff;
		this.anInt581 = this.anInt566 - this.anInt565;
		this.anInt551 = this.minimumStartColor >> 8 & 0xff;
		this.anInt599 = this.maximumStartColor >> 8 & 0xff;
		this.anInt584 = this.anInt599 - this.anInt551;
		this.anInt585 = this.minimumStartColor & 0xff;
		this.anInt586 = this.maximumStartColor & 0xff;
		this.anInt587 = this.anInt586 - this.anInt585;
		this.anInt588 = this.minimumStartColor >> 24 & 0xff;
		this.anInt575 = this.maximumStartColor >> 24 & 0xff;
		this.anInt590 = this.anInt575 - this.anInt588;
		if (this.fadeColor != 0) {
			this.colorFadeStart = this.colorFading * this.maximumLifetime / 100;
			this.alphaFadeStart = this.alphaFading * this.maximumLifetime / 100;
			if (this.colorFadeStart == 0) {
				this.colorFadeStart = 1;
			}

			this.fadeRedStep = ((this.fadeColor >> 16 & 0xff) - (this.anInt581 / 2 + this.anInt565) << 8) / this.colorFadeStart;
			this.fadeGreenStep = ((this.fadeColor >> 8 & 0xff) - (this.anInt584 / 2 + this.anInt551) << 8) / this.colorFadeStart;
			this.fadeBlueStep = ((this.fadeColor & 0xff) - (this.anInt587 / 2 + this.anInt585) << 8) / this.colorFadeStart;
			if (this.alphaFadeStart == 0) {
				this.alphaFadeStart = 1;
			}

			this.fadeAlphaStep = ((this.fadeColor >> 24 & 0xff) - (this.anInt590 / 2 + this.anInt588) << 8) / this.alphaFadeStart;
			this.fadeRedStep += this.fadeRedStep > 0 ? -4 : 4;
			this.fadeGreenStep += this.fadeGreenStep > 0 ? -4 : 4;
			this.fadeBlueStep += this.fadeBlueStep > 0 ? -4 : 4;
			this.fadeAlphaStep += this.fadeAlphaStep > 0 ? -4 : 4;
		}

		if (this.endSpeed != -1) {
			this.startSpeedChange = this.maximumLifetime * this.speedChange / 100;
			if (this.startSpeedChange == 0) {
				this.startSpeedChange = 1;
			}

			this.speedStep = (this.endSpeed - ((this.maximumSpeed - this.minimumSpeed) / 2 + this.minimumSpeed)) / this.startSpeedChange;
		}

		if (this.endSize != -1) {
			this.startSizeChange = this.sizeChange * this.maximumLifetime / 100;
			if (this.startSizeChange == 0) {
				this.startSizeChange = 1;
			}

			this.sizeChangeStep = (this.endSize - ((this.maximumSize - this.minimumSize) / 2 + this.minimumSize)) / this.startSizeChange;
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
