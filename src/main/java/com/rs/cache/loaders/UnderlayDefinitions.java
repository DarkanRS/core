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

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.lib.io.InputStream;

public final class UnderlayDefinitions {

	private static final ConcurrentHashMap<Integer, UnderlayDefinitions> defs = new ConcurrentHashMap<Integer, UnderlayDefinitions>();

	public int r;
	public int b;
	public int anInt6003;
	public boolean aBool6004;
	public boolean aBool6005;
	public int rgb = 0;
	public int g;
	public int a;
	public int texture;
	public int id;
    public int hue;
    public int saturation;
    public int lumiance;
    public int blendHue;
    public int blendHueMultiplier;
    public int hsl16;

	public static final UnderlayDefinitions getUnderlayDefinitions(int id) {
		UnderlayDefinitions def = defs.get(id);
		if (def != null)// open new txt document
			return def;
		byte[] data = Cache.STORE.getIndex(IndexType.CONFIG).getFile(ArchiveType.UNDERLAYS.getId(), id);
		def = new UnderlayDefinitions();
		def.id = id;
		if (data != null)
			def.readValueLoop(new InputStream(data));
		defs.put(id, def);
		return def;
	}

	private UnderlayDefinitions() {
		anInt6003 = -1;
		texture = -1;
		aBool6004 = true;
		aBool6005 = true;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

    @SuppressWarnings("unused")
	private void rgbToHsl(int rgb) {
        double r = (rgb >> 16 & 0xff) / 256.0;
        double g = (rgb >> 8 & 0xff) / 256.0;
        double b = (rgb & 0xff) / 256.0;
        double min = r;
        if (g < min) {
            min = g;
        }
        if (b < min) {
            min = b;
        }
        double max = r;
        if (g > max) {
            max = g;
        }
        if (b > max) {
            max = b;
        }
        double h = 0.0;
        double s = 0.0;
        double l = (min + max) / 2.0;
        if (min != max) {
            if (l < 0.5) {
                s = (max - min) / (max + min);
            }
            if (l >= 0.5) {
                s = (max - min) / (2.0 - max - min);
            }
            if (r == max) {
                h = (g - b) / (max - min);
            } else if (g == max) {
                h = 2.0 + (b - r) / (max - min);
            } else if (b == max) {
                h = 4.0 + (r - g) / (max - min);
            }
        }
        h /= 6.0;
        hue = (int) (h * 256.0);
        saturation = (int) (s * 256.0);
        lumiance = (int) (l * 256.0);
        if (saturation < 0) {
            saturation = 0;
        } else if (saturation > 255) {
            saturation = 255;
        }
        if (lumiance < 0) {
            lumiance = 0;
        } else if (lumiance > 255) {
            lumiance = 255;
        }
        if (l > 0.5) {
            blendHueMultiplier = (int) ((1.0 - l) * s * 512.0);
        } else {
            blendHueMultiplier = (int) (l * s * 512.0);
        }
        if (blendHueMultiplier < 1) {
            blendHueMultiplier = 1;
        }
        blendHue = (int) (h * blendHueMultiplier);
        hsl16 = hsl24to16(hue, saturation, lumiance);
    }
    
    public static Color toRGB(float h, float s, float l, float alpha) {
		if (s < 0.0f || s > 100.0f) {
			String message = "Color parameter outside of expected range - Saturation";
			throw new IllegalArgumentException(message);
		}

		if (l < 0.0f || l > 100.0f) {
			String message = "Color parameter outside of expected range - Luminance";
			throw new IllegalArgumentException(message);
		}

		if (alpha < 0.0f || alpha > 1.0f) {
			String message = "Color parameter outside of expected range - Alpha";
			throw new IllegalArgumentException(message);
		}

		// Formula needs all values between 0 - 1.

		h = h % 360.0f;
		h /= 360f;
		s /= 100f;
		l /= 100f;

		float q = 0;

		if (l < 0.5)
			q = l * (1 + s);
		else
			q = (l + s) - (s * l);

		float p = 2 * l - q;

		float r = Math.max(0, hueToRGB(p, q, h + (1.0f / 3.0f)));
		float g = Math.max(0, hueToRGB(p, q, h));
		float b = Math.max(0, hueToRGB(p, q, h - (1.0f / 3.0f)));

		r = Math.min(r, 1.0f);
		g = Math.min(g, 1.0f);
		b = Math.min(b, 1.0f);

		return new Color(r, g, b, alpha);
	}

	private static  float hueToRGB(float p, float q, float h) {
		if (h < 0)
			h += 1;

		if (h > 1)
			h -= 1;

		if (6 * h < 1) {
			return p + ((q - p) * 6 * h);
		}

		if (2 * h < 1) {
			return q;
		}

		if (3 * h < 2) {
			return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
		}

		return p;
	}

    private final static int hsl24to16(int h, int s, int l) {
        if (l > 179) {
            s /= 2;
        }
        if (l > 192) {
            s /= 2;
        }
        if (l > 217) {
            s /= 2;
        }
        if (l > 243) {
            s /= 2;
        }
        return (h / 4 << 10) + (s / 32 << 7) + l / 2;
    }

	private void readValues(InputStream stream, int i) {
		if (1 == i) {
			rgb = stream.read24BitInt();
			//rgbToHsl(rgb);
			r = (rgb >> 16 & 0xff);
			g = (rgb >> 8 & 0xff);
			b = (rgb & 0xff);
		} else if (i == 2) {
			texture = stream.readUnsignedShort();
			if (65535 == texture)
				texture = -1;
		} else if (3 == i)
			anInt6003 = (stream.readUnsignedShort() << 2);
		else if (i == 4)
			aBool6004 = false;
		else if (i == 5)
			aBool6005 = false;
	}

}
