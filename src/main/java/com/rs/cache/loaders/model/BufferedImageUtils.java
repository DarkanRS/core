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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Set;

import com.google.common.collect.Sets;

public class BufferedImageUtils {
	public static BufferedImage flipHoriz(BufferedImage image) {
	    BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D gg = newImage.createGraphics();
	    gg.drawImage(image, image.getHeight(), 0, -image.getWidth(), image.getHeight(), null);
	    gg.dispose();
	    return newImage;
	}
	
	/**
	 * Returns <code>true</code> if the provided image has partially transparent
	 * areas (alpha channel).
	 */
	public static boolean hasPartialTransparency(BufferedImage image) {
		final Raster alphaRaster = image.getAlphaRaster();
		if (image.getTransparency() != Transparency.TRANSLUCENT || alphaRaster == null) {
			return false;
		}

		int[] pixels = alphaRaster.getPixels(0, 0, alphaRaster.getWidth(), alphaRaster.getHeight(), (int[]) null);
		for (int i : pixels) {
			if (i != 0 && i != 255) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns <code>true</code> if the provided image has any kind of transparent
	 * areas
	 */
	public static boolean hasTransparency(BufferedImage image) {
		final Raster alphaRaster = image.getAlphaRaster();
		if (image.getTransparency() != Transparency.TRANSLUCENT || alphaRaster == null) {
			return false;
		}

		int[] pixels = alphaRaster.getPixels(0, 0, alphaRaster.getWidth(), alphaRaster.getHeight(), (int[]) null);
		for (int i : pixels) {
			if (i != 255) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the number of distinct colors (excluding transparency) in the
	 * <code>image</code>.
	 */
	public static int countDistictColors(BufferedImage image) {
		return getDistictColors(image).length;
	}

	/**
	 * Returns the <code>image</code>'s distinct colors in an RGB format, discarding
	 * transparency information.
	 */
	public static int[] getDistictColors(BufferedImage image) {
		return getDistictColors(image, 0);
	}

	/**
	 * Returns the <code>image</code>'s distinct colors in an RGB format, discarding
	 * transparency information. Adds <code>padding</code> empty slots at the
	 * beginning of the returned array.
	 */
	public static int[] getDistictColors(BufferedImage image, int padding) {
		final int width = image.getWidth();
		final int height = image.getHeight();

		final Set<Integer> colors = Sets.newHashSet();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int pixel = image.getRGB(x, y);

				// Count only colors for which alpha is not fully transparent
				if ((pixel & 0xff000000) != 0x00000000) {
					colors.add(Integer.valueOf(pixel & 0x00ffffff));
				}
			}
		}

		final int[] colorMap = new int[colors.size() + padding];
		int index = padding;
		for (Integer color : colors) {
			colorMap[index++] = color;
		}

		return colorMap;
	}

	/**
	 * Returns a two dimensional array of the <code>image</code>'s RGB values,
	 * including transparency.
	 */
	public static int[][] getRgb(BufferedImage image) {
		final int width = image.getWidth();
		final int height = image.getHeight();

		final int[][] rgb = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rgb[x][y] = image.getRGB(x, y);
			}
		}

		return rgb;
	}

	/**
	 * Performs matting of the <code>source</code> image using
	 * <code>matteColor</code>. Matting is rendering partial transparencies using
	 * solid color as if the original image was put on top of a bitmap filled with
	 * <code>matteColor</code>.
	 */
	public static BufferedImage matte(BufferedImage source, Color matteColor) {
		final int width = source.getWidth();
		final int height = source.getHeight();

		// A workaround for possibly different custom image types we can get:
		// draw a copy of the image
		final BufferedImage sourceConverted = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		sourceConverted.getGraphics().drawImage(source, 0, 0, null);

		final BufferedImage matted = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

		final BufferedImage matte = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		final int matteRgb = matteColor.getRGB();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				matte.setRGB(x, y, matteRgb);
			}
		}

		CompositeContext context = AlphaComposite.DstOver.createContext(matte.getColorModel(), sourceConverted.getColorModel(), null);
		context.compose(matte.getRaster(), sourceConverted.getRaster(), matted.getRaster());

		return matted;
	}

	/**
	 * Draws <code>image</code> on the <code>canvas</code> placing the top left
	 * corner of <code>image</code> at <code>x</code> / <code>y</code> offset from
	 * the top left corner of <code>canvas</code>.
	 */
	public static void drawImage(BufferedImage image, BufferedImage canvas, int x, int y) {
		final int[] imgRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		canvas.setRGB(x, y, image.getWidth(), image.getHeight(), imgRGB, 0, image.getWidth());
	}

	private BufferedImageUtils() {
	}
}
