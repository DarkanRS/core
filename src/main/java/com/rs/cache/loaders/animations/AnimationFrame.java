package com.rs.cache.loaders.animations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class AnimationFrame {
	public static short[] indicesBuffer = new short[500];
	public static short[] bufferX = new short[500];
	public static short[] bufferY = new short[500];
	public static short[] bufferZ = new short[500];
	public static short[] skipped = new short[500];
	public static byte[] flagsBuffer = new byte[500];
	
	public boolean modifiesColor;
	public int transformationCount = 0;
	public short[] transformationX;
	public short[] transformationY;
	public short[] transformationZ;
	public short[] skippedReferences;
	public byte[] transformationFlags;
	public boolean modifiesAlpha = false;
	public short[] transformationIndices;
	public boolean aBool988;
	public int frameBaseId;
	public AnimationFrameBase frameBase;
	
	public static AnimationFrame getFrame(byte[] frameData, AnimationFrameBase frameBase) {
		AnimationFrame frame = new AnimationFrame();
		frame.frameBase = frameBase;
		frame.frameBaseId = frameBase.id;
		frame.readFrameData(frameData);
		return frame;
	}
	
	private void readFrameData(byte[] data) {
		try {
			InputStream attribBuffer = new InputStream(data);
			InputStream transformationBuffer = new InputStream(data);
			attribBuffer.readUnsignedByte();
			attribBuffer.skip(2);
			int count = attribBuffer.readUnsignedByte();
			int used = 0;
			int last = -1;
			int lastUsed = -1;
			
			transformationBuffer.setOffset(attribBuffer.getOffset() + count);
			
			for (int i = 0; i < count; i++) {
				int type = frameBase.transformationTypes[i];
				if (type == 0)
					last = i;
				
				int attribute = attribBuffer.readUnsignedByte();
				if (attribute > 0) {
					if (type == 0)
						lastUsed = i;
					
					indicesBuffer[used] = (short) i;
					short value = 0;
					if (type == 3 || type == 10)
						value = (short) 128;
					if ((attribute & 0x1) != 0)
						bufferX[used] = (short) transformationBuffer.readSignedSmart();
					else
						bufferX[used] = value;
					if ((attribute & 0x2) != 0)
						bufferY[used] = (short) transformationBuffer.readSignedSmart();
					else
						bufferY[used] = value;
					if ((attribute & 0x4) != 0)
						bufferZ[used] = (short) transformationBuffer.readSignedSmart();
					else
						bufferZ[used] = value;
					flagsBuffer[used] = (byte) (attribute >>> 3 & 0x3);
					if (type == 2 || type == 9) {
						bufferX[used] = (short) (bufferX[used] << 2 & 0x3fff);
						bufferY[used] = (short) (bufferY[used] << 2 & 0x3fff);
						bufferZ[used] = (short) (bufferZ[used] << 2 & 0x3fff);
					}
					skipped[used] = -1;
					if (type == 1 || type == 2 || type == 3) {
						if (last > lastUsed) {
							skipped[used] = (short) last;
							lastUsed = last;
						}
					} else if (type == 5)
						modifiesAlpha = true;
					else if (type == 7)
						modifiesColor = true;
					else if (type == 9 || type == 10 || type == 8)
						aBool988 = true;
					used++;
				}
			}
			
			if (transformationBuffer.getOffset() != data.length)
				throw new RuntimeException();
			
			transformationCount = used;
			transformationIndices = new short[used];
			transformationX = new short[used];
			transformationY = new short[used];
			transformationZ = new short[used];
			skippedReferences = new short[used];
			transformationFlags = new byte[used];
			for (int i = 0; i < used; i++) {
				transformationIndices[i] = indicesBuffer[i];
				transformationX[i] = bufferX[i];
				transformationY[i] = bufferY[i];
				transformationZ[i] = bufferZ[i];
				skippedReferences[i] = skipped[i];
				transformationFlags[i] = flagsBuffer[i];
			}
		} catch (Exception exception_13) {
			this.transformationCount = 0;
			this.modifiesAlpha = false;
			this.modifiesColor = false;
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
