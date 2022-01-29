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
package com.rs.cache.loaders.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.rs.cache.Store;

public class RS3Tex {

	private int id;
	private byte[] data;
	private int[] pixels;
	private byte[] imageData;
	private BufferedImage image;

	public RS3Tex(int id) {
		this.id = id;
	}

	public void decode(Store cache, boolean quantize) {
		data = cache.getIndices()[43].getFile(id, 0);
		final byte format = (byte) (data[0] & 0xFF);
		try {
			switch (format) {
			case 1:
				byte[] tempPixels = new byte[data.length - 5];
				System.arraycopy(data, 5, tempPixels, 0, data.length - 5);
				pixels = method2671(tempPixels, false, quantize);
				break;
			case 6:
				int[] var14 = null;
				int offset = 1;
				for (int i = 0; i < 6; ++i) {
					int someLength = (data[offset] & 255) << 24 | (data[1 + offset] & 255) << 16 | (data[offset + 2] & 255) << 8 | data[3 + offset] & 255;
					byte[] var11 = new byte[someLength];
					System.arraycopy(data, offset + 4, var11, 0, someLength);
					int[] var12 = method2671(var11, false, quantize);
					if (i == 0) {
						var14 = new int[var12.length * 6];
					}
					System.arraycopy(var12, 0, var14, var12.length * i, var12.length);
					offset += 4 + someLength;
				}
				pixels = var14;
				break;
			default:
				throw new IllegalStateException("Unknown format=" + format);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int[] method2671(byte[] imageData, boolean var2, boolean quantize) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
		this.imageData = imageData;
		if (quantize)
			image = ColorQuantizer.quantize(image, Color.BLACK);
		this.image = BufferedImageUtils.flipHoriz(image);
		if (image == null) {
			return null;
		} else {
			int[] var5 = getRGB(image);
			if (var2) {
				for (int var6 = image.getHeight() - 1; var6 >= 0; --var6) {
					int var7 = var6 * image.getWidth();
					for (int var8 = (var6 + 1) * image.getWidth(); var7 < var8; ++var7) {
						--var8;
						int var9 = var5[var7];
						var5[var7] = var5[var8];
						var5[var8] = var9;
					}
				}
			}
			return var5;
		}
	}

	private static int[] getRGB(BufferedImage bufferedimage) {
		if (bufferedimage.getType() == 10 || bufferedimage.getType() == 0) {
			int[] is = null;
			is = bufferedimage.getRaster().getPixels(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), is);
			int[] is_6_ = (new int[bufferedimage.getWidth() * bufferedimage.getHeight()]);
			if (bufferedimage.getType() == 10) {
				for (int i_7_ = 0; i_7_ < is_6_.length; i_7_++) {
					is_6_[i_7_] = is[i_7_] + ((is[i_7_] << 16) + (is[i_7_] << 8)) + -16777216;
				}
			} else {
				for (int i_8_ = 0; i_8_ < is_6_.length; i_8_++) {
					int i_9_ = 2 * i_8_;
					is_6_[i_8_] = ((is[i_9_ + 1] << 24) + is[i_9_] + ((is[i_9_] << 16) + (is[i_9_] << 8)));
				}
			}
			return is_6_;
		}
		return bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
	}

	public ByteBuffer toByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);
		for (int pixel : pixels) {
			buffer.put((byte) ((pixel >> 16) & 0xFF));
			buffer.put((byte) ((pixel >> 8) & 0xFF));
			buffer.put((byte) (pixel & 0xFF));
			buffer.put((byte) (pixel >> 24));
		}
		buffer.flip();
		return buffer;
	}
	
	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	public int[] getPixels() {
		return pixels;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public BufferedImage getImage() {
		return image;
	}

}
