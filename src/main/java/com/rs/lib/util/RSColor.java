package com.rs.lib.util;

import java.awt.Color;

public class RSColor {
	public static final int HUE_MAX = 63;
	public static final int SATURATION_MAX = 7;
	public static final int LUMINANCE_MAX = 127;
	
	private int hue, saturation, luminance;
	
	private RSColor(int hue, int saturation, int luminance) {
		this.hue = hue;
		this.saturation = saturation;
		this.luminance = luminance;
	}
	
	public int getValue() {
		return packHSL(hue, saturation, luminance);
	}
	
	public RSColor adjustHue(int amount) {
		hue = Utils.clampI(hue+amount, 0, HUE_MAX);
		return this;
	}
	
	public RSColor adjustSaturation(int amount) {
		saturation = Utils.clampI(saturation+amount, 0, SATURATION_MAX);
		return this;
	}
	
	public RSColor adjustLuminance(int amount) {
		luminance = Utils.clampI(luminance+amount, 0, LUMINANCE_MAX);
		return this;
	}
	
	public static RSColor fromHSL(int hsl) {
		return new RSColor(unpackHue(hsl), unpackSaturation(hsl), unpackLuminance(hsl));
	}

	public static short packHSL(int hue, int saturation, int luminance) {
		return (short) ((short) (hue & HUE_MAX) << 10 | (short) (saturation & SATURATION_MAX) << 7 | (short) (luminance & LUMINANCE_MAX));
	}

	public static int unpackHue(int hsl) {
		return hsl >> 10 & HUE_MAX;
	}

	public static int unpackSaturation(int hsl) {
		return hsl >> 7 & SATURATION_MAX;
	}

	public static int unpackLuminance(int hsl) {
		return hsl & LUMINANCE_MAX;
	}

	public static String formatHSL(int hsl) {
		return String.format("%02Xh%Xs%02Xl", unpackHue(hsl), unpackSaturation(hsl), unpackLuminance(hsl));
	}

	public static int rgbToHSL(int rgb, double brightness) {
		if (rgb == 1)
			return 0;

		brightness = 1.D / brightness;

		double r = (double) (rgb >> 16 & 255) / 256.0D;
		double g = (double) (rgb >> 8 & 255) / 256.0D;
		double b = (double) (rgb & 255) / 256.0D;

		r = Math.pow(r, brightness);
		g = Math.pow(g, brightness);
		b = Math.pow(b, brightness);

		float[] hsv = Color.RGBtoHSB((int) (r * 256.D), (int) (g * 256.D), (int) (b * 256.D), null);
		double hue = hsv[0];
		double luminance = hsv[2] - ((hsv[2] * hsv[1]) / 2.F);
		double saturation = (hsv[2] - luminance) / Math.min(luminance, 1 - luminance);

		return packHSL((int) (Math.ceil(hue * 64.D) % 63.D), (int) Math.ceil(saturation * 7.D), (int) Math.ceil(luminance * 127.D));
	}
	
	public static int RGB_to_HSL(int red, int green, int blue) {
		float[] HSB = Color.RGBtoHSB(red, green, blue, null);
		float hue = (HSB[0]);
		float saturation = (HSB[1]);
		float brightness = (HSB[2]);
		int encode_hue = (int) (hue * 63); // to 6-bits
		int encode_saturation = (int) (saturation * 7); // to 3-bits
		int encode_brightness = (int) (brightness * 127); // to 7-bits
		return (encode_hue << 10) + (encode_saturation << 7) + (encode_brightness);
	}

	public static int HSL_to_RGB(int hsl) {
		int decode_hue = (hsl >> 10) & 0x3f;
		int decode_saturation = (hsl >> 7) & 0x07;
		int decode_brightness = (hsl & 0x7f);
		return Color.HSBtoRGB((float) decode_hue / 63, (float) decode_saturation / 7, (float) decode_brightness / 127);
	}
}
