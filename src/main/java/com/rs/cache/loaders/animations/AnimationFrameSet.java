package com.rs.cache.loaders.animations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;
import com.rs.lib.util.Utils;

public class AnimationFrameSet {
	
	private static final ConcurrentHashMap<Integer, AnimationFrameSet> FRAME_COLLECTIONS = new ConcurrentHashMap<Integer, AnimationFrameSet>();
	
	public int id;
	private AnimationFrame[] frames;
	
	public AnimationFrame[] getFrames() {
		return frames;
	}
		
	public static AnimationFrameSet getFrameSet(int id) {
		if (FRAME_COLLECTIONS.get(id) != null)
			return FRAME_COLLECTIONS.get(id);
		if (id > Cache.STORE.getIndex(IndexType.ANIMATION_FRAME_SETS).getTable().getArchives().length)
			return null;
		int[] files = Cache.STORE.getIndex(IndexType.ANIMATION_FRAME_SETS).getTable().getArchives()[id].getValidFileIds();
		if (files == null) {
			System.out.println("Null files: " + id);
			return null;
		}
		
		AnimationFrameSet defs = new AnimationFrameSet();
		defs.id = id;
		byte[][] frameData = new byte[files.length][];
		for (int i = 0;i < files.length;i++)
			frameData[i] = Cache.STORE.getIndex(IndexType.ANIMATION_FRAME_SETS).getFile(id, files[i]);
		
		defs.frames = new AnimationFrame[frameData.length];
		for (int i = 0;i < frameData.length;i++) {
			InputStream stream = new InputStream(frameData[i]);
			stream.setOffset(1);
			int frameBaseId = stream.readUnsignedShort();
			defs.frames[i] = AnimationFrame.getFrame(frameData[i], AnimationFrameBase.getFrame(frameBaseId));
		}
		
		FRAME_COLLECTIONS.put(id, defs);
		return defs;
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
