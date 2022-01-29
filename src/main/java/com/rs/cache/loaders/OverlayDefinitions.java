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

import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;

public final class OverlayDefinitions {

	private static final ConcurrentHashMap<Integer, OverlayDefinitions> defs = new ConcurrentHashMap<Integer, OverlayDefinitions>();

	public int anInt6318;
	int anInt6319;
	public int primaryRgb;
	public int texture = -1;
	public int anInt6322;
	public int secondaryRgb;
	public int anInt6325;
	public int anInt6326;
	public boolean aBool6327;
	public int anInt6328;
	public int anInt6329;
	public int anInt6330;
	public boolean aBool6331;
	public int anInt6332;
	public boolean hideUnderlay = true;
	public int id;

	public static final OverlayDefinitions getOverlayDefinitions(int id) {
		OverlayDefinitions script = defs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.OVERLAYS.getId(), id);
		script = new OverlayDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		defs.put(id, script);
		return script;
	}
	
//	public int getRGB() {
//		int rgb = 0;
//		if (hideUnderlay) {
//            rgb = primaryRgb;
//        }
//        if (secondaryRgb > -1) {
//            rgb = secondaryRgb;
//        }
//        if (texture != -1) {
//        	rgb = TextureDefinitions.getDefinitions(texture & 0xFF).unk4;
//		}
//        if (rgb == 0 || rgb == -1 || rgb == 16711935) {
//        	rgb = 0;
//		}
//		return rgb;
//	}
	
	public int getOverlayRGB() {
		int col = primaryRgb == -1 || primaryRgb == 16711935 || primaryRgb == 0 ? secondaryRgb : primaryRgb;
		if (col == 0 || col == -1 || col == 16711935) {
			col = 0;
		}
		if (col == 0 && texture != -1) {
			col = TextureDefinitions.getDefinitions(texture & 0xFF).color;
		}
		return col;
	}
	
	public int getShapeRGB() {
		int col = primaryRgb == -1 || primaryRgb == 16711935 || primaryRgb == 0 ? secondaryRgb : primaryRgb;
		if (col == 0 || col == -1 || col == 16711935) {
			col = 0;
		}
		return col;
	}

	OverlayDefinitions() {
		secondaryRgb = -1;
		anInt6322 = -1;
		aBool6331 = true;
		anInt6326 = -1;
		aBool6327 = false;
		anInt6328 = -1;
		anInt6329 = -1;
		anInt6330 = -1;
		anInt6318 = -1;
		anInt6325 = -1;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	static int method2632(int i) {
		if (i == 16711935)
			return -1;
		return method11651(i);
	}

	public static int method11651(int i) {
		double d = (double) (i >> 16 & 0xff) / 256.0;
		double d_13_ = (double) (i >> 8 & 0xff) / 256.0;
		double d_14_ = (double) (i & 0xff) / 256.0;
		double d_15_ = d;
		if (d_13_ < d_15_)
			d_15_ = d_13_;
		if (d_14_ < d_15_)
			d_15_ = d_14_;
		double d_16_ = d;
		if (d_13_ > d_16_)
			d_16_ = d_13_;
		if (d_14_ > d_16_)
			d_16_ = d_14_;
		double d_17_ = 0.0;
		double d_18_ = 0.0;
		double d_19_ = (d_16_ + d_15_) / 2.0;
		if (d_15_ != d_16_) {
			if (d_19_ < 0.5)
				d_18_ = (d_16_ - d_15_) / (d_15_ + d_16_);
			if (d_19_ >= 0.5)
				d_18_ = (d_16_ - d_15_) / (2.0 - d_16_ - d_15_);
			if (d == d_16_)
				d_17_ = (d_13_ - d_14_) / (d_16_ - d_15_);
			else if (d_16_ == d_13_)
				d_17_ = 2.0 + (d_14_ - d) / (d_16_ - d_15_);
			else if (d_16_ == d_14_)
				d_17_ = 4.0 + (d - d_13_) / (d_16_ - d_15_);
		}
		d_17_ /= 6.0;
		int i_20_ = (int) (d_17_ * 256.0);
		int i_21_ = (int) (d_18_ * 256.0);
		int i_22_ = (int) (d_19_ * 256.0);
		if (i_21_ < 0)
			i_21_ = 0;
		else if (i_21_ > 255)
			i_21_ = 255;
		if (i_22_ < 0)
			i_22_ = 0;
		else if (i_22_ > 255)
			i_22_ = 255;
		if (i_22_ > 243)
			i_21_ >>= 4;
		else if (i_22_ > 217)
			i_21_ >>= 3;
		else if (i_22_ > 192)
			i_21_ >>= 2;
		else if (i_22_ > 179)
			i_21_ >>= 1;
		return ((i_22_ >> 1) + (((i_20_ & 0xff) >> 2 << 10) + (i_21_ >> 5 << 7)));
	}

	private void readValues(InputStream stream, int i) {
		if (1 == i)
			primaryRgb = stream.read24BitInt();
			//rgb = method2632(stream.read24BitInt());
		else if (i == 2)
			texture = stream.readUnsignedByte();
		else if (3 == i) {
			texture = stream.readUnsignedShort();
			if (65535 == texture)
				texture = -1;
		} else if (i == 5)
			hideUnderlay = false;
		else if (i == 7)
			secondaryRgb = stream.read24BitInt();/*
												 * method2632(stream.read24BitInt( ), -398738558);
												 */
		else if (8 != i) {
			if (i == 9)
				anInt6322 = ((stream.readUnsignedShort() << 2));
			else if (i == 10)
				aBool6331 = false;
			else if (i == 11)
				anInt6326 = stream.readUnsignedByte();
			else if (i == 12)
				aBool6327 = true;
			else if (i == 13)
				anInt6328 = stream.read24BitInt();
			else if (14 == i)
				anInt6329 = (stream.readUnsignedByte() << 2);
			else if (16 == i)
				anInt6330 = stream.readUnsignedByte();
			else if (i == 20)
				anInt6318 = stream.readUnsignedShort();
			else if (i == 21)
				anInt6332 = stream.readUnsignedByte();
			else if (i == 22)
				anInt6325 = stream.readUnsignedShort();
		}
	}

}
