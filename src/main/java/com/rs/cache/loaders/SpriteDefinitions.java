package com.rs.cache.loaders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.util.ArrayList;
import java.util.Arrays;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;

public final class SpriteDefinitions {

	private BufferedImage[] images;

	private int pallete[];
	private int pixelsIndexes[][];
	private byte alpha[][];
	private boolean[] usesAlpha;
	private int biggestWidth;
	private int biggestHeight;

	public SpriteDefinitions(BufferedImage... images) {
		this.images = images;
	}
	
	public static SpriteDefinitions getSprite(int spriteId, int fileId) {
		return new SpriteDefinitions(Cache.STORE, spriteId, fileId);
	}

	public SpriteDefinitions(Store cache, int archiveId, int fileId) {
		decodeArchive(cache, archiveId, fileId);
	}

	public void decodeArchive(Store cache, int archiveId, int fileId) {
		byte[] data = cache.getIndex(IndexType.SPRITES).getFile(archiveId, fileId);
		if (data == null)
			return;
		InputStream stream = new InputStream(data);
		stream.setOffset(data.length - 2);
		int count = stream.readUnsignedShort();
		images = new BufferedImage[count];
		pixelsIndexes = new int[images.length][];
		alpha = new byte[images.length][];
		usesAlpha = new boolean[images.length];
		int[] imagesMinX = new int[images.length];
		int[] imagesMinY = new int[images.length];
		int[] imagesWidth = new int[images.length];
		int[] imagesHeight = new int[images.length];
		stream.setOffset(data.length - 7 - images.length * 8);
		setBiggestWidth(stream.readShort()); // biggestWidth
		setBiggestHeight(stream.readShort()); // biggestHeight
		int palleteLength = (stream.readUnsignedByte() & 0xff) + 1; // 1 + up to
		// 255.
		for (int index = 0; index < images.length; index++)
			imagesMinX[index] = stream.readUnsignedShort();
		for (int index = 0; index < images.length; index++)
			imagesMinY[index] = stream.readUnsignedShort();
		for (int index = 0; index < images.length; index++)
			imagesWidth[index] = stream.readUnsignedShort();
		for (int index = 0; index < images.length; index++)
			imagesHeight[index] = stream.readUnsignedShort();
		stream.setOffset(data.length - 7 - images.length * 8 - (palleteLength - 1) * 3);
		pallete = new int[palleteLength];
		for (int index = 1; index < palleteLength; index++) {
			pallete[index] = stream.read24BitInt();
			if (pallete[index] == 0)
				pallete[index] = 1;
		}
		stream.setOffset(0);
		for (int i_20_ = 0; i_20_ < images.length; i_20_++) {
			int pixelsIndexesLength = imagesWidth[i_20_] * imagesHeight[i_20_];
			pixelsIndexes[i_20_] = new int[pixelsIndexesLength];
			alpha[i_20_] = new byte[pixelsIndexesLength];
			int maskData = stream.readUnsignedByte();
			if ((maskData & 0x2) == 0) {
				if ((maskData & 0x1) == 0) {
					for (int index = 0; index < pixelsIndexesLength; index++) {
						pixelsIndexes[i_20_][index] = (byte) stream.readByte();
					}
				} else {
					for (int i_24_ = 0; i_24_ < imagesWidth[i_20_]; i_24_++) {
						for (int i_25_ = 0; i_25_ < imagesHeight[i_20_]; i_25_++) {
							pixelsIndexes[i_20_][i_24_ + i_25_ * imagesWidth[i_20_]] = (byte) stream.readByte();
						}
					}
				}
			} else {
				usesAlpha[i_20_] = true;
				boolean bool = false;
				if ((maskData & 0x1) == 0) {
					for (int index = 0; index < pixelsIndexesLength; index++) {
						pixelsIndexes[i_20_][index] = (byte) stream.readByte();
					}
					for (int i_27_ = 0; i_27_ < pixelsIndexesLength; i_27_++) {
						byte i_28_ = (alpha[i_20_][i_27_] = (byte) stream.readByte());
						bool = bool | i_28_ != -1;
					}
				} else {
					for (int i_29_ = 0; i_29_ < imagesWidth[i_20_]; i_29_++) {
						for (int i_30_ = 0; i_30_ < imagesHeight[i_20_]; i_30_++) {
							pixelsIndexes[i_20_][i_29_ + i_30_ * imagesWidth[i_20_]] = stream.readByte();
						}
					}
					for (int i_31_ = 0; i_31_ < imagesWidth[i_20_]; i_31_++) {
						for (int i_32_ = 0; i_32_ < imagesHeight[i_20_]; i_32_++) {
							byte i_33_ = (alpha[i_20_][i_31_ + i_32_ * imagesWidth[i_20_]] = (byte) stream.readByte());
							bool = bool | i_33_ != -1;
						}
					}
				}
				if (!bool)
					alpha[i_20_] = null;
			}
			images[i_20_] = getBufferedImage(imagesWidth[i_20_], imagesHeight[i_20_], pixelsIndexes[i_20_], alpha[i_20_], usesAlpha[i_20_]);
		}
	}

	public BufferedImage getBufferedImage(int width, int height, int[] pixelsIndexes, byte[] extraPixels, boolean useExtraPixels) {
		if (width <= 0 || height <= 0)
			return null;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		int[] rgbArray = new int[width * height];
		int i = 0;
		int i_43_ = 0;
		if (useExtraPixels && extraPixels != null) {
			for (int i_44_ = 0; i_44_ < height; i_44_++) {
				for (int i_45_ = 0; i_45_ < width; i_45_++) {
					rgbArray[i_43_++] = (extraPixels[i] << 24 | (pallete[pixelsIndexes[i] & 0xff]));
					i++;
				}
			}
		} else {
			for (int i_46_ = 0; i_46_ < height; i_46_++) {
				for (int i_47_ = 0; i_47_ < width; i_47_++) {
					int i_48_ = pallete[pixelsIndexes[i++] & 0xff];
					rgbArray[i_43_++] = i_48_ != 0 ? ~0xffffff | i_48_ : 0;
				}
			}
		}
		image.setRGB(0, 0, width, height, rgbArray, 0, width);
		image.flush();
		return image;
	}

	public byte[] encodeFile() {
		if (pallete == null) // if not generated yet
			generatePallete();
		OutputStream stream = new OutputStream();
		// sets pallete indexes and int size bytes
		for (int imageId = 0; imageId < images.length; imageId++) {
			int pixelsMask = 0;
			if (usesAlpha[imageId])
				pixelsMask |= 0x2;
			// pixelsMask |= 0x1; //sets read all rgbarray indexes 1by1
			stream.writeByte(pixelsMask);
			for (int index = 0; index < pixelsIndexes[imageId].length; index++)
				stream.writeByte(pixelsIndexes[imageId][index]);
			if (usesAlpha[imageId])
				for (int index = 0; index < alpha[imageId].length; index++)
					stream.writeByte(alpha[imageId][index]);
		}

		// sets up to 255colors pallete, index0 is black
		for (int index = 1; index < pallete.length; index++)
			stream.write24BitInt(pallete[index]);

		// extra inform
		if (biggestWidth == 0 && biggestHeight == 0) {
			for (BufferedImage image : images) {
				if (image.getWidth() > biggestWidth)
					biggestWidth = image.getWidth();
				if (image.getHeight() > biggestHeight)
					biggestHeight = image.getHeight();
			}
		}
		stream.writeShort(biggestWidth); // probably used for textures
		stream.writeShort(biggestHeight);// probably used for textures
		stream.writeByte(pallete.length - 1); // sets pallete size, -1 cuz of
		// black index
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getMinX());
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getMinY());
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getWidth());
		for (int imageId = 0; imageId < images.length; imageId++)
			stream.writeShort(images[imageId].getHeight());
		stream.writeShort(images.length); // amt of images
		// generates fixed byte data array
		byte[] container = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(container, 0, container.length);
		return container;
	}

	public int getPalleteIndex(int rgb) {
		if (pallete == null) // index 0 is 0
			pallete = new int[] { 0 };
		for (int index = 0; index < pallete.length; index++) {
			if (pallete[index] == rgb)
				return index;
		}
		if (pallete.length == 256) {
			System.out.println("Pallete to big, please reduce images quality.");
			return 0;
		}
		int[] newpallete = new int[pallete.length + 1];
		System.arraycopy(pallete, 0, newpallete, 0, pallete.length);
		newpallete[pallete.length] = rgb;
		pallete = newpallete;
		return pallete.length - 1;
	}

	public int addImage(BufferedImage image) {
		BufferedImage[] newImages = Arrays.copyOf(images, images.length + 1);
		newImages[images.length] = image;
		images = newImages;
		pallete = null;
		pixelsIndexes = null;
		alpha = null;
		usesAlpha = null;
		return images.length - 1;
	}

	public void replaceImage(BufferedImage image, int index) {
		images[index] = image;
		pallete = null;
		pixelsIndexes = null;
		alpha = null;
		usesAlpha = null;
	}

	public void generatePallete() {
		pixelsIndexes = new int[images.length][];
		alpha = new byte[images.length][];
		usesAlpha = new boolean[images.length];
		for (int index = 0; index < images.length; index++) {
			BufferedImage image = filter(images[index]);
			int[] rgbArray = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
			pixelsIndexes[index] = new int[image.getWidth() * image.getHeight()];
			alpha[index] = new byte[image.getWidth() * image.getHeight()];
			for (int pixel = 0; pixel < pixelsIndexes[index].length; pixel++) {
				int rgb = rgbArray[pixel];
				Color c = new Color(rgb, true);
				int medintrgb = new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB();
				int i = getPalleteIndex(medintrgb);
				pixelsIndexes[index][pixel] = i;
				if (c.getAlpha() != 0) {
					alpha[index][pixel] = (byte) c.getAlpha();
					usesAlpha[index] = true;
				}
			}
		}
	}

	public BufferedImage[] getImages() {
		return images;
	}

	public int getBiggestWidth() {
		return biggestWidth;
	}

	public void setBiggestWidth(int biggestWidth) {
		this.biggestWidth = biggestWidth;
	}

	public int getBiggestHeight() {
		return biggestHeight;
	}

	public void setBiggestHeight(int biggestHeight) {
		this.biggestHeight = biggestHeight;
	}

	public static BufferedImage filter(BufferedImage image) {
		DetailFilter filter = new DetailFilter();
		while (!filter.done()) {
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					int rgb = filter.filterRGB(x, y, image.getRGB(x, y));
					image.setRGB(x, y, rgb);
				}
			}
		}
		return image;
	}

	private static class DetailFilter extends RGBImageFilter {

		public static int START_DIFF = 1;

		private int diffB, diffG, diffR;

		private ArrayList<Integer> colours;

		public boolean done() {
			if (colours == null) {
				colours = new ArrayList<Integer>();
				diffB = diffG = diffR = START_DIFF;
				return false;
			}

			int increase = colours.size() > 1000 ? 5 : 1;
			if (increase == 0)
				increase = 1;

			// System.out.println(diffR+", "+diffG+", "+diffB+",
			// "+colours.size()+", "+increase);
			if (colours.size() <= 256)
				return true;
			colours.clear();

			if (diffR <= diffB)
				diffR += increase;
			else if (diffG <= diffB)
				diffG += increase;
			else if (diffB <= diffR)
				diffB += increase;
			return false;

		}

		@Override
		public int filterRGB(int x, int y, int rgb) {
			Color c = new Color(rgb, true);
			rgb = c.getRGB();
			int blue = (int) (c.getBlue() / diffB * diffB);
			int green = (int) (c.getGreen() / diffG * diffG);
			int red = (int) (c.getRed() / diffR * diffR);
			int alpha = c.getAlpha();

			rgb = new Color(red, green, blue).getRGB();
			if (!colours.contains(rgb)) {
				// System.out.println(colours.size() +", "+rgb+", "+red+",
				// "+green+", "+blue+", "+alpha);
				colours.add(rgb);
			}
			rgb = new Color(red, green, blue, alpha).getRGB();
			return rgb;
		}
	}

}
