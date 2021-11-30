package com.rs.lib.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.reflect.ClassPath;
import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.cache.loaders.EnumDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.QCMesDefinitions.QCValueType;
import com.rs.cache.loaders.StructDefinitions;
import com.rs.cache.loaders.animations.AnimationFrame;
import com.rs.cache.loaders.animations.AnimationFrameSet;
import com.rs.cache.loaders.cs2.CS2Instruction;
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.cache.loaders.sound.Instrument;
import com.rs.lib.Constants;
import com.rs.lib.game.Item;
import com.rs.lib.game.WorldTile;

public final class Utils {

	private static final Object ALGORITHM_LOCK = new Object();
	private static final Random RANDOM = new SecureRandom();

	public static final int[] ROTATION_DIR_X = { -1, 0, 1, 0 };
	public static final int[] ROTATION_DIR_Y = { 0, 1, 0, -1 };
	
	public static DecimalFormat FORMAT = new DecimalFormat("###,###.##");

	private static final byte[][] ANGLE_DIRECTION_DELTA = { { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } };
	
	public static char[] CP_1252_CHARACTERS = { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021', '\u02c6', 
			'\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c', 
			'\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', 
			'\0', '\u017e', '\u0178' };
	
	public String getLevelDifferenceColor(int hiddenLevel, int displayedLevel) {
		int levelDifference = displayedLevel - hiddenLevel;
		
		if (levelDifference > 0)
			return String.format("%02X%02X%02X", 255, levelDifference > 10 ? 0 : 255 - (25 * levelDifference), 0); 
		else if (levelDifference < 0)
			return String.format("%02X%02X%02X", levelDifference < -10 ? 0 : 255 - (25 * Math.abs(levelDifference)), 255, 0); 
		else
			return String.format("%02X%02X%02X", 255, 255, 0); 
	}
	
	public static int RGB_to_RS2HSB(int red, int green, int blue) {
		float[] HSB = Color.RGBtoHSB(red, green, blue, null);
		float hue = (HSB[0]);
		float saturation = (HSB[1]);
		float brightness = (HSB[2]);
		int encode_hue = (int) (hue * 63); // to 6-bits
		int encode_saturation = (int) (saturation * 7); // to 3-bits
		int encode_brightness = (int) (brightness * 127); // to 7-bits
		return (encode_hue << 10) + (encode_saturation << 7) + (encode_brightness);
	}

	public static int RS2HSB_to_RGB(int RS2HSB) {
		int decode_hue = (RS2HSB >> 10) & 0x3f;
		int decode_saturation = (RS2HSB >> 7) & 0x07;
		int decode_brightness = (RS2HSB & 0x7f);
		return Color.HSBtoRGB((float) decode_hue / 63, (float) decode_saturation / 7, (float) decode_brightness / 127);
	}

	public static int[] randSum(int numVars, int total) {
	    double randNums[] = new double[numVars], sum = 0;

	    for (int i = 0; i < randNums.length; i++) {
	        randNums[i] = Math.random();
	        sum += randNums[i];
	    }

	    for (int i = 0; i < randNums.length; i++) {
	    	randNums[i] = randNums[i] / sum * total;
	    }

	    int[] randInt = new int[numVars];
	    for (int i = 0;i < numVars;i++)
	    	randInt[i] = (int) randNums[i];
	    
	    return randInt;
	}
	
	public static int interfaceIdFromHash(int hash) {
		return hash >> 16;
	}
	
	public static int componentIdFromHash(int hash) {
		return hash - ((hash >> 16) << 16);
	}
	
	public static Map<String, Object> cloneMap(Map<String, Object> from) {
		if (from == null)
			return null;
		Map<String, Object> newMap = new HashMap<String, Object>();

		for (Entry<String, Object> entry : from.entrySet()) {
			newMap.put(entry.getKey(), entry.getValue());
		}
		
		return newMap;
	}
	
	public static Object CS2ValTranslate(CS2Type valueType, Object o) {
		if (valueType == CS2Type.ICOMPONENT) {
			if (o instanceof String)
				return o;
			long interfaceId = ((int) o) >> 16;
			long componentId = ((int) o) - (interfaceId << 16);
			return "IComponent("+interfaceId+", "+componentId+")";
		} else if (valueType == CS2Type.LOCATION) {
			if (o instanceof String)
				return o;
			return new WorldTile(((int) o));
		} else if (valueType == CS2Type.SKILL) {
			if (o instanceof String)
				return o;
			int idx = (int) o;
			if (idx >= Constants.SKILL_NAME.length)
				return o;
			return idx+"("+Constants.SKILL_NAME[((int) o)]+")";
		} else if (valueType == CS2Type.ITEM) {
			if (o instanceof String)
				return o;
			return ((int) o)+" ("+ItemDefinitions.getDefs(((int) o)).getName()+")"; 
		} else if (valueType == CS2Type.NPCDEF) {
			if (o instanceof String)
				return o;
			return ((int) o)+" ("+NPCDefinitions.getDefs(((int) o)).getName()+")"; 
		} else if (valueType == CS2Type.STRUCT) {
			return o + ": " + StructDefinitions.getStruct((int) o); 
		} else if (valueType == CS2Type.ENUM) {
			return o + ": " + EnumDefinitions.getEnum((int) o); 
		}
		return o;
	}
	
	public static int[] range(int min, int max) {
		int[] range = new int[max-min+1];
		for (int i = min,j=0;i <= max;i++,j++)
			range[j] = i; 
		return range;
	}
	
	public static int[] range(int min, int max, int step) {
		int[] range = new int[(max-min)/step+1];
		for (int i = min,j=0;i <= max;i+=step,j++)
			range[j] = i; 
		return range;
	}

	public static String formatDouble(double d) {
		return FORMAT.format(d);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	 
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static int[] shuffleIntArray(int[] array) {
		int[] shuffledArray = new int[array.length];
		System.arraycopy(array, 0, shuffledArray, 0, array.length);

		for (int i = shuffledArray.length - 1; i > 0; i--) {
			int index = RANDOM.nextInt(i + 1);
			int a = shuffledArray[index];
			shuffledArray[index] = shuffledArray[i];
			shuffledArray[i] = a;
		}

		return shuffledArray;
	}
	
	public static int findClosestIdx(int[] arr, int target) {
		int distance = Math.abs(arr[0] - target);
		int idx = 0;
		for(int c = 1; c < arr.length; c++){
		    int cdistance = Math.abs(arr[c] - target);
		    if(cdistance < distance){
		        idx = c;
		        distance = cdistance;
		    }
		}
		return idx;
	}
	
	public static double clampD(double val, double min, double max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	public static int clampI(int val, int min, int max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	public static long gcm(long a, long b) {
	    return b == 0 ? a : gcm(b, a % b); // Not bad for one line of code :)
	}
	
	public static int[] asFractionArr(long a, long b) {
		 int gcm = (int) gcm(a, b);
		 return new int[] { (int) (a / gcm), (int) (b / gcm) };
	}

	public static String asFraction(long a, long b) {
	    long gcm = gcm(a, b);
	    return (a / gcm) + "/" + (b / gcm);
	}
	
	public static Rational toRational(double number) {
		return toRational(number, 5);
	}
	
	public static boolean skillSuccess(int level, int rate1, int rate99) {
		return skillSuccess(level, 1.0, rate1, rate99);
	}
	
	public static boolean skillSuccess(int level, double percModifier, int rate1, int rate99) {
		rate1 = (int) (percModifier * (double) rate1);
		rate99 = (int) (percModifier * (double) rate99);
		double perc = (double) level / 99.0;
		int chance = clampI((int) ((double) rate1 + (((double) rate99 - (double) rate1) * perc)), 0, 256);
		return random(255) <= chance;
	}

	public static Rational toRational(double number, int largestRightOfDecimal) {

		long sign = 1;
		if (number < 0) {
			number = -number;
			sign = -1;
		}

		final long SECOND_MULTIPLIER_MAX = (long) Math.pow(10, largestRightOfDecimal - 1);
		final long FIRST_MULTIPLIER_MAX = SECOND_MULTIPLIER_MAX * 10L;
		final double ERROR = Math.pow(10, -largestRightOfDecimal - 1);
		long firstMultiplier = 1;
		long secondMultiplier = 1;
		boolean notIntOrIrrational = false;
		long truncatedNumber = (long) number;
		Rational rationalNumber = new Rational((long) (sign * number * FIRST_MULTIPLIER_MAX), FIRST_MULTIPLIER_MAX);

		double error = number - truncatedNumber;
		while ((error >= ERROR) && (firstMultiplier <= FIRST_MULTIPLIER_MAX)) {
			secondMultiplier = 1;
			firstMultiplier *= 10;
			while ((secondMultiplier <= SECOND_MULTIPLIER_MAX) && (secondMultiplier < firstMultiplier)) {
				double difference = (number * firstMultiplier) - (number * secondMultiplier);
				truncatedNumber = (long) difference;
				error = difference - truncatedNumber;
				if (error < ERROR) {
					notIntOrIrrational = true;
					break;
				}
				secondMultiplier *= 10;
			}
		}

		if (notIntOrIrrational) {
			rationalNumber = new Rational(sign * truncatedNumber, firstMultiplier - secondMultiplier);
		}
		return rationalNumber;
	}
	
	public static int getRSColor(int red, int green, int blue) {
		float[] HSB = Color.RGBtoHSB(red, green, blue, null);
		float hue = (HSB[0]);
		float saturation = (HSB[1]);
		float brightness = (HSB[2]);
		int encode_hue = (int) (hue * 63);			//to 6-bits
		int encode_saturation = (int) (saturation * 7);		//to 3-bits
		int encode_brightness = (int) (brightness * 127); 	//to 7-bits
		return (encode_hue << 10) + (encode_saturation << 7) + (encode_brightness);
	}

	public static int greatestCommonDenominator(int a, int b) {
		if (a == 0)
			return b;
		return greatestCommonDenominator(b % a, a);
	}

	public static int greatestCommonDenominator(int... arr) {
		int result = arr[0];
		for (int i = 1; i < arr.length; i++)
			result = greatestCommonDenominator(arr[i], result);

		return result;
	}
	
	public static int leastCommonMultiple(int a, int b) {
		return a * (b / greatestCommonDenominator(a, b));
	}

	public static int leastCommonMultiple(int... arr) {
		int result = arr[0];
		for (int i = 1; i < arr.length; i++)
			result = leastCommonMultiple(result, arr[i]);
		return result;
	}

	public static String quote(String str) {
		StringBuffer result = new StringBuffer("\"");
		for (int i = 0; i < str.length(); i++) {
			char c;
			switch (c = str.charAt(i)) {
				case '\0' :
					result.append("\\0");
					break;
				case '\t' :
					result.append("\\t");
					break;
				case '\n' :
					result.append("\\n");
					break;
				case '\r' :
					result.append("\\r");
					break;
				case '\\' :
					result.append("\\\\");
					break;
				case '\"' :
					result.append("\\\"");
					break;
				default :
					if (c < 32) {
						String oct = Integer.toOctalString(c);
						result.append("\\000".substring(0, 4 - oct.length())).append(oct);
					} else if (c >= 32 && c < 127)
						result.append(str.charAt(i));
					else {
						String hex = Integer.toHexString(c);
						result.append("\\u0000".substring(0, 6 - hex.length())).append(hex);
					}
			}
		}
		return result.append("\"").toString();
	}

	public static String quote(char c) {
		switch (c) {
			case '\0' :
				return "\'\\0\'";
			case '\t' :
				return "\'\\t\'";
			case '\n' :
				return "\'\\n\'";
			case '\r' :
				return "\'\\r\'";
			case '\\' :
				return "\'\\\\\'";
			case '\"' :
				return "\'\\\"\'";
			case '\'' :
				return "\'\\\'\'";
		}
		if (c < 32) {
			String oct = Integer.toOctalString(c);
			return "\'\\000".substring(0, 5 - oct.length()) + oct + "\'";
		}
		if (c >= 32 && c < 127)
			return "\'" + c + "\'";
		else {
			String hex = Integer.toHexString(c);
			return "\'\\u0000".substring(0, 7 - hex.length()) + hex + "\'";
		}
	}
	
	public static char cp1252ToChar(byte i) {
		int i_35_ = i & 0xff;
		if (0 == i_35_) {
			throw new IllegalArgumentException("Non cp1252 character 0x"+Integer.toString(i_35_, 16)+" provided");
		}
		if (i_35_ >= 128 && i_35_ < 160) {
			int i_36_ = CP_1252_CHARACTERS[i_35_ - 128];
			if (0 == i_36_) {
				i_36_ = 63;
			}
			i_35_ = i_36_;
		}
		return (char) i_35_;
	}

	public static byte charToCp1252(char c) {
		byte i_18_;
		if (c > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff') {
			i_18_ = (byte) c;
		} else if ('\u20ac' == c) {
			i_18_ = (byte) -128;
		} else if (c == '\u201a') {
			i_18_ = (byte) -126;
		} else if (c == '\u0192') {
			i_18_ = (byte) -125;
		} else if (c == '\u201e') {
			i_18_ = (byte) -124;
		} else if ('\u2026' == c) {
			i_18_ = (byte) -123;
		} else if ('\u2020' == c) {
			i_18_ = (byte) -122;
		} else if ('\u2021' == c) {
			i_18_ = (byte) -121;
		} else if ('\u02c6' == c) {
			i_18_ = (byte) -120;
		} else if ('\u2030' == c) {
			i_18_ = (byte) -119;
		} else if (c == '\u0160') {
			i_18_ = (byte) -118;
		} else if (c == '\u2039') {
			i_18_ = (byte) -117;
		} else if ('\u0152' == c) {
			i_18_ = (byte) -116;
		} else if (c == '\u017d') {
			i_18_ = (byte) -114;
		} else if ('\u2018' == c) {
			i_18_ = (byte) -111;
		} else if ('\u2019' == c) {
			i_18_ = (byte) -110;
		} else if ('\u201c' == c) {
			i_18_ = (byte) -109;
		} else if ('\u201d' == c) {
			i_18_ = (byte) -108;
		} else if ('\u2022' == c) {
			i_18_ = (byte) -107;
		} else if (c == '\u2013') {
			i_18_ = (byte) -106;
		} else if ('\u2014' == c) {
			i_18_ = (byte) -105;
		} else if (c == '\u02dc') {
			i_18_ = (byte) -104;
		} else if (c == '\u2122') {
			i_18_ = (byte) -103;
		} else if ('\u0161' == c) {
			i_18_ = (byte) -102;
		} else if (c == '\u203a') {
			i_18_ = (byte) -101;
		} else if ('\u0153' == c) {
			i_18_ = (byte) -100;
		} else if ('\u017e' == c) {
			i_18_ = (byte) -98;
		} else if (c == '\u0178') {
			i_18_ = (byte) -97;
		} else {
			i_18_ = (byte) 63;
		}
		return i_18_;
	}
	
	static int getNumOfChars(CharSequence str, char val) {
		int count = 0;
		int size = str.length();
		for (int i_5 = 0; i_5 < size; i_5++) {
			if (str.charAt(i_5) == val) {
				++count;
			}
		}
		return count;
	}
	
	public static String[] splitByChar(String str, char val) {
		int numChars = getNumOfChars(str, val);
		String[] arr_4 = new String[numChars + 1];
		int i_5 = 0;
		int i_6 = 0;

		for (int i_7 = 0; i_7 < numChars; i_7++) {
			int i_8;
			for (i_8 = i_6; str.charAt(i_8) != val; i_8++) {
				;
			}

			arr_4[i_5++] = str.substring(i_6, i_8);
			i_6 = i_8 + 1;
		}

		arr_4[numChars] = str.substring(i_6);
		return arr_4;
	}

	public static String readString(byte[] buffer, int i_1, int i_2) {
		char[] arr_4 = new char[i_2];
		int offset = 0;

		for (int i_6 = 0; i_6 < i_2; i_6++) {
			int i_7 = buffer[i_6 + i_1] & 0xff;
			if (i_7 != 0) {
				if (i_7 >= 128 && i_7 < 160) {
					char var_8 = CP_1252_CHARACTERS[i_7 - 128];
					if (var_8 == 0) {
						var_8 = 63;
					}

					i_7 = var_8;
				}

				arr_4[offset++] = (char) i_7;
			}
		}

		return new String(arr_4, 0, offset);
	}
	
	public static int writeString(CharSequence string, int start, int end, byte[] buffer, int offset) {
		int length = end - start;

		for (int i = 0; i < length; i++) {
			char c = string.charAt(i + start);
			if (c > 0 && c < 128 || c >= 160 && c <= 255) {
				buffer[i + offset] = (byte) c;
			} else if (c == 8364) {
				buffer[i + offset] = -128;
			} else if (c == 8218) {
				buffer[i + offset] = -126;
			} else if (c == 402) {
				buffer[i + offset] = -125;
			} else if (c == 8222) {
				buffer[i + offset] = -124;
			} else if (c == 8230) {
				buffer[i + offset] = -123;
			} else if (c == 8224) {
				buffer[i + offset] = -122;
			} else if (c == 8225) {
				buffer[i + offset] = -121;
			} else if (c == 710) {
				buffer[i + offset] = -120;
			} else if (c == 8240) {
				buffer[i + offset] = -119;
			} else if (c == 352) {
				buffer[i + offset] = -118;
			} else if (c == 8249) {
				buffer[i + offset] = -117;
			} else if (c == 338) {
				buffer[i + offset] = -116;
			} else if (c == 381) {
				buffer[i + offset] = -114;
			} else if (c == 8216) {
				buffer[i + offset] = -111;
			} else if (c == 8217) {
				buffer[i + offset] = -110;
			} else if (c == 8220) {
				buffer[i + offset] = -109;
			} else if (c == 8221) {
				buffer[i + offset] = -108;
			} else if (c == 8226) {
				buffer[i + offset] = -107;
			} else if (c == 8211) {
				buffer[i + offset] = -106;
			} else if (c == 8212) {
				buffer[i + offset] = -105;
			} else if (c == 732) {
				buffer[i + offset] = -104;
			} else if (c == 8482) {
				buffer[i + offset] = -103;
			} else if (c == 353) {
				buffer[i + offset] = -102;
			} else if (c == 8250) {
				buffer[i + offset] = -101;
			} else if (c == 339) {
				buffer[i + offset] = -100;
			} else if (c == 382) {
				buffer[i + offset] = -98;
			} else if (c == 376) {
				buffer[i + offset] = -97;
			} else {
				buffer[i + offset] = 63;
			}
		}

		return length;
	}

	public static void add(List<Item> list, Item[] items) {
		for (Item item : items) {
			list.add(item);
		}
	}

	public static Object getFieldValue(Object object, Field field) throws Throwable {
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == int[][].class) {
			return Arrays.deepToString((int[][]) field.get(object));
		} else if (type == AnimationFrameSet[].class) {
			return Arrays.toString((AnimationFrameSet[]) field.get(object));
		} else if (type == Instrument[].class) {
			return Arrays.toString((Instrument[]) field.get(object));
		} else if (type == HashMap[].class) {
			return Arrays.toString((HashMap[]) field.get(object));
		} else if (type == CS2Instruction[].class) {
			return Arrays.toString((CS2Instruction[]) field.get(object));
		} else if (type == CS2Type[].class) {
			return Arrays.toString((CS2Type[]) field.get(object));
		} else if (type == QCValueType[].class) {
			return Arrays.toString((QCValueType[]) field.get(object));
		} else if (type == AnimationFrame[].class) {
			return Arrays.toString((AnimationFrame[]) field.get(object));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(object));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(object));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(object));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(object));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(object));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(object));
		} else if (type == boolean[].class) {
			return Arrays.toString((boolean[]) field.get(object));
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(object));
		} else if (type == String[].class) {
			return Arrays.toString((String[]) field.get(object));
		}
		return field.get(object);
	}
	
    public static String wrap(final String str, final int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }
	
    public static String wrap(final String str, final int wrapLength, final String newLineStr, final boolean wrapLongWords) {
        return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
    }
	
	 public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords, String wrapOn) {
	        if (str == null) {
	            return null;
	        }
	        if (newLineStr == null) {
	            newLineStr = System.lineSeparator();
	        }
	        if (wrapLength < 1) {
	            wrapLength = 1;
	        }
	        if (wrapOn == null || wrapOn.equals("")) {
	            wrapOn = " ";
	        }
	        final Pattern patternToWrapOn = Pattern.compile(wrapOn);
	        final int inputLineLength = str.length();
	        int offset = 0;
	        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

	        while (offset < inputLineLength) {
	            int spaceToWrapAt = -1;
	            Matcher matcher = patternToWrapOn.matcher(
	                str.substring(offset, Math.min((int)Math.min(Integer.MAX_VALUE, offset + wrapLength + 1L), inputLineLength)));
	            if (matcher.find()) {
	                if (matcher.start() == 0) {
	                    offset += matcher.end();
	                    continue;
	                }
	                spaceToWrapAt = matcher.start() + offset;
	            }

	            // only last line without leading spaces is left
	            if(inputLineLength - offset <= wrapLength) {
	                break;
	            }

	            while(matcher.find()){
	                spaceToWrapAt = matcher.start() + offset;
	            }

	            if (spaceToWrapAt >= offset) {
	                // normal case
	                wrappedLine.append(str, offset, spaceToWrapAt);
	                wrappedLine.append(newLineStr);
	                offset = spaceToWrapAt + 1;

	            } else {
	                // really long word or URL
	                if (wrapLongWords) {
	                    // wrap really long word one line at a time
	                    wrappedLine.append(str, offset, wrapLength + offset);
	                    wrappedLine.append(newLineStr);
	                    offset += wrapLength;
	                } else {
	                    // do not wrap really long word, just extend beyond limit
	                    matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
	                    if (matcher.find()) {
	                        spaceToWrapAt = matcher.start() + offset + wrapLength;
	                    }

	                    if (spaceToWrapAt >= 0) {
	                        wrappedLine.append(str, offset, spaceToWrapAt);
	                        wrappedLine.append(newLineStr);
	                        offset = spaceToWrapAt + 1;
	                    } else {
	                        wrappedLine.append(str, offset, str.length());
	                        offset = inputLineLength;
	                    }
	                }
	            }
	        }

	        // Whatever is left in line is short enough to just pass through
	        wrappedLine.append(str, offset, str.length());

	        return wrappedLine.toString();
	    }

	public static byte[] getDirection(int angle) {
		int v = angle >> 11;
		return ANGLE_DIRECTION_DELTA[v];
	}

    public static int getClosestNumberFromArray(int[] numbers, int number) {
        int distance = Math.abs(numbers[0] - number);
        int idx = 0;
        for(int c = 1; c < numbers.length; c++){
            int cdistance = Math.abs(numbers[c] - number);
            if(cdistance < distance){
                idx = c;
                distance = cdistance;
            }
        }
        return numbers[idx];
    }

	public static String ticksToTime(double ticks) {
		long millis = Math.round(ticks) * 600;
		int seconds = (int) (millis / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;

		minutes = minutes - (hours * 60);
		seconds = seconds - (hours * 60 * 60) - (minutes * 60);

		String time = "";
		if (hours > 0)
			time += "" + hours + " hours ";
		if (minutes > 0)
			time += "" + minutes + " minutes ";
		if (seconds > 0)
			time += "" + seconds + " seconds ";
		if (time == "")
			time = "moment or two";
		time = time.trim() + ".";
		return time;
	}

	public static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

	// public static int getMapArchiveId(int regionX, int regionY) {
	// return regionX | regionY << 7;
	// }

	public static int getTodayDate() {
		Calendar cal = new GregorianCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		return (month * 100 + day);
	}

	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	private static Calendar cal = Calendar.getInstance();

	public static String getDateString() {
		return dateFormat.format(cal.getTime());
	}

	public static String formatNumber(int number) {
		return NumberFormat.getNumberInstance(Locale.US).format(number);
	}

	public static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,###").format(amount);
	}

	public static String getFormattedNumber(double amount, char seperator) {
		String str = new DecimalFormat("#,###,###").format(amount);
		char[] rebuff = new char[str.length()];
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9')
				rebuff[i] = c;
			else
				rebuff[i] = seperator;
		}
		return new String(rebuff);
	}

	public static String formatTypicalInteger(int integer) {
		return NumberFormat.getInstance().format(integer);
	}

	public static String formatLong(long longg) {
		return NumberFormat.getInstance().format(longg);
	}

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}

	public static final byte[] encryptUsingMD5(byte[] buffer) {
		// prevents concurrency problems with the algorithm
		synchronized (ALGORITHM_LOCK) {
			try {
				MessageDigest algorithm = MessageDigest.getInstance("MD5");
				algorithm.update(buffer);
				byte[] digest = algorithm.digest();
				algorithm.reset();
				return digest;
			} catch (Throwable e) {
				Logger.handle(e);
			}
			return null;
		}
	}

	public static boolean inCircle(WorldTile location, WorldTile center, int radius) {
		return getDistance(center, location) < radius;
	}

	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
	
	public static final int getDistanceI(WorldTile t1, WorldTile t2) {
		return getDistanceI(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}

	public static final int getDistanceI(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public static final double getDistance(WorldTile t1, WorldTile t2) {
		return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}

	public static final double getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}

	public static final boolean isQCValid(int id) {
		return Cache.STORE.getIndex(IndexType.QC_MESSAGES).fileExists(1, id);
	}

	public static final int[][] getCoordOffsetsNear(int size) {
		int[] xs = new int[4 + (4 * size)];
		int[] xy = new int[xs.length];
		xs[0] = -size;
		xy[0] = 1;
		xs[1] = 1;
		xy[1] = 1;
		xs[2] = -size;
		xy[2] = -size;
		xs[3] = 1;
		xy[2] = -size;
		for (int fakeSize = size; fakeSize > 0; fakeSize--) {
			xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4))] = 1;
			xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
			xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
			xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
		}
		return new int[][] { xs, xy };
	}
	
	public static final int getAngleTo(WorldTile fromTile, WorldTile toTile) {
		return getAngleTo(toTile.getX() - fromTile.getX(), toTile.getY() - fromTile.getY());
	}

	public static final int getAngleTo(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}
	
	public static final int[] getBackFace(int dir) {
		double ang = dir / 2607.5945876176133;
		return new int[] { (int) Math.round(Math.sin(ang)), (int) Math.round(Math.cos(ang)) };
	}
	
	public static final int[] getFrontFace(int dir) {
		int[] front = getBackFace(dir);
		return new int[] { front[0] * -1, front[1] * -1 };
	}
	
//	public static final Direction getMoveDirection(int xOffset, int yOffset) {
//		if (xOffset < 0) {
//			if (yOffset < 0)
//				return 5;
//			else if (yOffset > 0)
//				return 0;
//			else
//				return 3;
//		} else if (xOffset > 0) {
//			if (yOffset < 0)
//				return 7;
//			else if (yOffset > 0)
//				return 2;
//			else
//				return 4;
//		} else {
//			if (yOffset < 0)
//				return 6;
//			else if (yOffset > 0)
//				return 1;
//			else
//				return -1;
//		}
//	}

	public static final int getSpotAnimDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndex(IndexType.SPOT_ANIMS).getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndex(IndexType.SPOT_ANIMS).getValidFilesCount(lastArchiveId);
	}

	public static final int getAnimationDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndex(IndexType.ANIMATIONS).getLastArchiveId();
		return lastArchiveId * 128 + Cache.STORE.getIndex(IndexType.ANIMATIONS).getValidFilesCount(lastArchiveId);
	}

	public static final int getBASAnimDefSize() {
		return Cache.STORE.getIndex(IndexType.CONFIG).getValidFilesCount(ArchiveType.BAS.getId());
	}

	public static final int getConfigDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndex(IndexType.VARBITS).getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndex(IndexType.VARBITS).getValidFilesCount(lastArchiveId);
	}

	public static final int getObjectDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndex(IndexType.OBJECTS).getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndex(IndexType.OBJECTS).getValidFilesCount(lastArchiveId);
	}

	public static final int getNPCDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndex(IndexType.NPCS).getLastArchiveId();
		return lastArchiveId * 128 + Cache.STORE.getIndex(IndexType.NPCS).getValidFilesCount(lastArchiveId);
	}

	// 22314

	public static final int getItemDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndex(IndexType.ITEMS).getLastArchiveId();
		return (lastArchiveId * 256 + Cache.STORE.getIndex(IndexType.ITEMS).getValidFilesCount(lastArchiveId));
	}
	
	public static final int getItemDefinitionsSize(Store store) {
		int lastArchiveId = store.getIndex(IndexType.ITEMS).getLastArchiveId();
		return (lastArchiveId * 256 + store.getIndex(IndexType.ITEMS).getValidFilesCount(lastArchiveId));
	}
	
	public static final int getVarbitDefinitionsSize() {
		return Cache.STORE.getIndex(IndexType.VARBITS).getLastArchiveId() * 0x3ff;
	}

	public static boolean itemExists(int id) {
		if (id >= getItemDefinitionsSize())
			return false;
		return Cache.STORE.getIndex(IndexType.ITEMS).fileExists(id >>> 8, 0xff & id);
	}

	public static final int getInterfaceDefinitionsSize() {
		return Cache.STORE.getIndex(IndexType.INTERFACES).getLastArchiveId() + 1;
	}

	public static final int getInterfaceDefinitionsComponentsSize(int interfaceId) {
		return Cache.STORE.getIndex(IndexType.INTERFACES).getLastFileId(interfaceId) + 1;
	}

	public static String formatPlayerNameForProtocol(String name) {
		if (name == null)
			return "";
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}

	public static String formatPlayerNameForDisplay(String name) {
		if (name == null)
			return "";
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}

	public static final int getRandomInclusive(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}

	public static final int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}
	
	public static final int randomInclusive(int min, int max) {
		return random(min, max+1);
	}

	public static final double random(double min, double max) {
		return min + (max - min) * RANDOM.nextDouble();
	}
	
	public static final double randomD() {
		return RANDOM.nextDouble();
	}

	public static final int next(int max, int min) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static final double random(double maxValue) {
		return random(0.0, maxValue);
	}

	public static final int random(int maxValue) {
		if (maxValue <= 0)
			return 0;
		return RANDOM.nextInt(maxValue);
	}

	public static final long randomLong(long n) {
		long bits, val;
		do {
			bits = (RANDOM.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits - val + (n - 1) < 0L);
		return val;
	}

	public static final String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static final char[] SUPER_VALID_CHARS = { ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public static boolean invalidAccountName(String name) {
		return name.length() == 0 || name.length() > 12 || name.startsWith("_") || name.endsWith("_") || name.contains("__") || containsInvalidCharacter(name);
	}

	public static boolean invalidAuthId(String auth) {
		return auth.length() != 10 || auth.contains("_") || containsInvalidCharacter(auth);
	}

	public static boolean containsBadCharacter(char c) {
		for (char vc : SUPER_VALID_CHARS) {
			if (vc == c)
				return false;
		}
		return true;
	}

	public static boolean containsInvalidCharacter(char c) {
		for (char vc : VALID_CHARS) {
			if (vc == c)
				return false;
		}
		return true;
	}

	public static boolean containsInvalidCharacter(String name) {
		for (char c : name.toCharArray()) {
			if (containsInvalidCharacter(c))
				return true;
		}
		return false;
	}

	public static boolean containsBadCharacter(String name) {
		for (char c : name.toCharArray()) {
			if (containsBadCharacter(c))
				return true;
		}
		return false;
	}

	public static final long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static final int packGJString2(int position, byte[] buffer, String String) {
		int length = String.length();
		int offset = position;
		for (int index = 0; length > index; index++) {
			int character = String.charAt(index);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	public static final int calculateGJString2Length(String String) {
		int length = String.length();
		int gjStringLength = 0;
		for (int index = 0; length > index; index++) {
			char c = String.charAt(index);
			if (c > '\u007f') {
				if (c <= '\u07ff')
					gjStringLength += 2;
				else
					gjStringLength += 3;
			} else
				gjStringLength++;
		}
		return gjStringLength;
	}

	public static final int getNameHash(String name) {
		name = name.toLowerCase();
		int hash = 0;
		for (int index = 0; index < name.length(); index++)
			hash = method1258(name.charAt(index)) + ((hash << 5) - hash);
		return hash;
	}

	public static final byte method1258(char c) {
		byte charByte;
		if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
			charByte = (byte) c;
		} else if (c != '\u20AC') {
			if (c != '\u201A') {
				if (c != '\u0192') {
					if (c == '\u201E') {
						charByte = -124;
					} else if (c != '\u2026') {
						if (c != '\u2020') {
							if (c == '\u2021') {
								charByte = -121;
							} else if (c == '\u02C6') {
								charByte = -120;
							} else if (c == '\u2030') {
								charByte = -119;
							} else if (c == '\u0160') {
								charByte = -118;
							} else if (c == '\u2039') {
								charByte = -117;
							} else if (c == '\u0152') {
								charByte = -116;
							} else if (c != '\u017D') {
								if (c == '\u2018') {
									charByte = -111;
								} else if (c != '\u2019') {
									if (c != '\u201C') {
										if (c == '\u201D') {
											charByte = -108;
										} else if (c != '\u2022') {
											if (c == '\u2013') {
												charByte = -106;
											} else if (c == '\u2014') {
												charByte = -105;
											} else if (c == '\u02DC') {
												charByte = -104;
											} else if (c == '\u2122') {
												charByte = -103;
											} else if (c != '\u0161') {
												if (c == '\u203A') {
													charByte = -101;
												} else if (c != '\u0153') {
													if (c == '\u017E') {
														charByte = -98;
													} else if (c != '\u0178') {
														charByte = 63;
													} else {
														charByte = -97;
													}
												} else {
													charByte = -100;
												}
											} else {
												charByte = -102;
											}
										} else {
											charByte = -107;
										}
									} else {
										charByte = -109;
									}
								} else {
									charByte = -110;
								}
							} else {
								charByte = -114;
							}
						} else {
							charByte = -122;
						}
					} else {
						charByte = -123;
					}
				} else {
					charByte = -125;
				}
			} else {
				charByte = -126;
			}
		} else {
			charByte = -128;
		}
		return charByte;
	}

	public static char[] aCharArray6385 = { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e', '\u0178' };

	public static final String getUnformatedMessage(int messageDataLength, int messageDataOffset, byte[] messageData) {
		char[] cs = new char[messageDataLength];
		int i = 0;
		for (int i_6_ = 0; i_6_ < messageDataLength; i_6_++) {
			int i_7_ = 0xff & messageData[i_6_ + messageDataOffset];
			if ((i_7_ ^ 0xffffffff) != -1) {
				if ((i_7_ ^ 0xffffffff) <= -129 && (i_7_ ^ 0xffffffff) > -161) {
					int i_8_ = aCharArray6385[i_7_ - 128];
					if (i_8_ == 0)
						i_8_ = 63;
					i_7_ = i_8_;
				}
				cs[i++] = (char) i_7_;
			}
		}
		return new String(cs, 0, i);
	}

	public static final byte[] getFormatedMessage(String message) {
		int i_0_ = message.length();
		byte[] is = new byte[i_0_];
		for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > (i_0_ ^ 0xffffffff); i_1_++) {
			int i_2_ = message.charAt(i_1_);
			if (((i_2_ ^ 0xffffffff) >= -1 || i_2_ >= 128) && (i_2_ < 160 || i_2_ > 255)) {
				if ((i_2_ ^ 0xffffffff) != -8365) {
					if ((i_2_ ^ 0xffffffff) == -8219)
						is[i_1_] = (byte) -126;
					else if ((i_2_ ^ 0xffffffff) == -403)
						is[i_1_] = (byte) -125;
					else if (i_2_ == 8222)
						is[i_1_] = (byte) -124;
					else if (i_2_ != 8230) {
						if ((i_2_ ^ 0xffffffff) != -8225) {
							if ((i_2_ ^ 0xffffffff) != -8226) {
								if ((i_2_ ^ 0xffffffff) == -711)
									is[i_1_] = (byte) -120;
								else if (i_2_ == 8240)
									is[i_1_] = (byte) -119;
								else if ((i_2_ ^ 0xffffffff) == -353)
									is[i_1_] = (byte) -118;
								else if ((i_2_ ^ 0xffffffff) != -8250) {
									if (i_2_ == 338)
										is[i_1_] = (byte) -116;
									else if (i_2_ == 381)
										is[i_1_] = (byte) -114;
									else if ((i_2_ ^ 0xffffffff) == -8217)
										is[i_1_] = (byte) -111;
									else if (i_2_ == 8217)
										is[i_1_] = (byte) -110;
									else if (i_2_ != 8220) {
										if (i_2_ == 8221)
											is[i_1_] = (byte) -108;
										else if ((i_2_ ^ 0xffffffff) == -8227)
											is[i_1_] = (byte) -107;
										else if ((i_2_ ^ 0xffffffff) != -8212) {
											if (i_2_ == 8212)
												is[i_1_] = (byte) -105;
											else if ((i_2_ ^ 0xffffffff) != -733) {
												if (i_2_ != 8482) {
													if (i_2_ == 353)
														is[i_1_] = (byte) -102;
													else if (i_2_ != 8250) {
														if ((i_2_ ^ 0xffffffff) == -340)
															is[i_1_] = (byte) -100;
														else if (i_2_ != 382) {
															if (i_2_ == 376)
																is[i_1_] = (byte) -97;
															else
																is[i_1_] = (byte) 63;
														} else
															is[i_1_] = (byte) -98;
													} else
														is[i_1_] = (byte) -101;
												} else
													is[i_1_] = (byte) -103;
											} else
												is[i_1_] = (byte) -104;
										} else
											is[i_1_] = (byte) -106;
									} else
										is[i_1_] = (byte) -109;
								} else
									is[i_1_] = (byte) -117;
							} else
								is[i_1_] = (byte) -121;
						} else
							is[i_1_] = (byte) -122;
					} else
						is[i_1_] = (byte) -123;
				} else
					is[i_1_] = (byte) -128;
			} else
				is[i_1_] = (byte) i_2_;
		}
		return is;
	}

	public static int getHashMapSize(int size) {
		size--;
		size |= size >>> -1810941663;
		size |= size >>> 2010624802;
		size |= size >>> 10996420;
		size |= size >>> 491045480;
		size |= size >>> 1388313616;
		return 1 + size;
	}

	/**
	 * Walk dirs 0 - South-West 1 - South 2 - South-East 3 - West 4 - East 5 -
	 * North-West 6 - North 7 - North-East
	 */
	public static int getPlayerWalkingDirection(int dx, int dy) {
		if (dx == -1 && dy == -1) {
			return 0;
		}
		if (dx == 0 && dy == -1) {
			return 1;
		}
		if (dx == 1 && dy == -1) {
			return 2;
		}
		if (dx == -1 && dy == 0) {
			return 3;
		}
		if (dx == 1 && dy == 0) {
			return 4;
		}
		if (dx == -1 && dy == 1) {
			return 5;
		}
		if (dx == 0 && dy == 1) {
			return 6;
		}
		if (dx == 1 && dy == 1) {
			return 7;
		}
		return -1;
	}

	public static int getPlayerRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2)
			return 0;
		if (dx == -1 && dy == -2)
			return 1;
		if (dx == 0 && dy == -2)
			return 2;
		if (dx == 1 && dy == -2)
			return 3;
		if (dx == 2 && dy == -2)
			return 4;
		if (dx == -2 && dy == -1)
			return 5;
		if (dx == 2 && dy == -1)
			return 6;
		if (dx == -2 && dy == 0)
			return 7;
		if (dx == 2 && dy == 0)
			return 8;
		if (dx == -2 && dy == 1)
			return 9;
		if (dx == 2 && dy == 1)
			return 10;
		if (dx == -2 && dy == 2)
			return 11;
		if (dx == -1 && dy == 2)
			return 12;
		if (dx == 0 && dy == 2)
			return 13;
		if (dx == 1 && dy == 2)
			return 14;
		if (dx == 2 && dy == 2)
			return 15;
		return -1;
	}

	public static ArrayList<Class<?>> getClassesWithAnnotation(String packageName, Class<? extends Annotation> annotation) throws ClassNotFoundException, IOException {
		ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (ClassPath.ClassInfo info : cp.getTopLevelClassesRecursive(packageName)) {
			if (!Class.forName(info.getName()).isAnnotationPresent(annotation))
				continue;
			classes.add(Class.forName(info.getName()));
		}
		return classes;
	}
	
	public static ArrayList<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (ClassPath.ClassInfo info : cp.getTopLevelClassesRecursive(packageName)) {
			classes.add(Class.forName(info.getName()));
		}
		return classes;
	}

	public static String[] SYMBOLS = { ":", ";" };

	public static boolean isSymbol(String line) {
		for (int i = 0; i < SYMBOLS.length; i++) {
			if (line.equals(SYMBOLS[i])) {
				return true;
			}
		}
		return false;
	}

	public static String fixChatMessage(String message) {
		StringBuilder sb = new StringBuilder();
		boolean space = false;
		boolean forcedCaps = true;
		for (int i = 0; i < message.length(); i++) {
			if (forcedCaps) {
				if (String.valueOf(message.charAt(i)).equals(" ") || isSymbol(String.valueOf(message.charAt(i)))) {
					sb.append(message.charAt(i));
				} else {
					sb.append(String.valueOf(message.charAt(i)).toUpperCase());
					forcedCaps = false;
				}
			} else {
				String line = String.valueOf(message.charAt(i));
				if (line.equals("?") || line.equals("!") || line.equals(".") || line.equals(":") || line.equals(";")) {
					forcedCaps = true;
					sb.append(line);
				} else if (line.equals(" ")) {
					space = true;
					sb.append(line);
				} else if (line.equals(line.toUpperCase())) {
					if (space) {
						sb.append(line);
						space = false;
					} else {
						sb.append(line.toLowerCase());
					}
				} else {
					sb.append(line);
					space = false;
					forcedCaps = false;
				}
			}
		}
		return sb.toString();
	}

	public static int getProjectileTime(int i, WorldTile startTile, WorldTile endTile, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset, int creatorSize) {
		int distance = (int) (Utils.getDistance(startTile, endTile) + 1);
		if (speed == 0) // cant be 0, happens cuz method wrong and so /10 needed
			// so may round to 0
			speed = 1;
		return (int) ((delay * 10) + (distance
				* ((30 / speed) * 10) /** Math.cos(Math.toRadians(curve)) */
		));
	}

	public static int getProjectileTime(WorldTile startTile, WorldTile endTile, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset, int creatorSize) {
		int distance = (int) (Utils.getDistance(startTile, endTile) + 1);
		if (speed == 0) // cant be 0, happens cuz method wrong and so /10 needed
			// so may round to 0
			speed = 1;
		return (delay * 10) + (distance
				* ((30 / speed) * 10) /** Math.cos(Math.toRadians(curve)) */
		);
	}

	public static int getProjectileTimeNew(WorldTile from, int fromSizeX, int fromSizeY, WorldTile to, int toSizeX, int toSizeY, double speed) {
		int fromX = from.getX() * 2 + fromSizeX;
		int fromY = from.getY() * 2 + fromSizeY;

		int toX = to.getX() * 2 + toSizeX;
		int toY = to.getY() * 2 + toSizeY;

		fromX /= 2;
		fromY /= 2;
		toX /= 2;
		toY /= 2;

		int deltaX = fromX - toX;
		int deltaY = fromY - toY;
		int sqrt = (int) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
		return (int) (sqrt * (10 / speed));
	}

	public static int getProjectileTimeSoulsplit(WorldTile from, int fromSizeX, int fromSizeY, WorldTile to, int toSizeX, int toSizeY) {
		int fromX = from.getX() * 2 + fromSizeX;
		int fromY = from.getY() * 2 + fromSizeY;

		int toX = to.getX() * 2 + toSizeX;
		int toY = to.getY() * 2 + toSizeY;

		fromX /= 2;
		fromY /= 2;
		toX /= 2;
		toY /= 2;

		int deltaX = fromX - toX;
		int deltaY = fromY - toY;
		int sqrt = (int) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
		sqrt *= 15;
		sqrt -= sqrt % 30;
		return Math.max(30, sqrt);
	}

	public static int projectileTimeToCycles(int time) {
		return (time + 29) / 30;
		/* return Math.max(1, (time+14)/30); */
	}
	
	public static boolean isWithin(int i, int min, int max) {
		if (i > min && i < max)
			return true;
		return false;
	}
	
	public static boolean isWithinInclusive(int i, int min, int max) {
		if (i >= min && i <= max)
			return true;
		return false;
	}

	public static String formatTime(long time) {
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 24;
		StringBuilder string = new StringBuilder();
		string.append(hours > 9 ? hours : ("0" + hours));
		string.append(":" + (minutes > 9 ? minutes : ("0" + minutes)));
		string.append(":" + (seconds > 9 ? seconds : ("0" + seconds)));
		return string.toString();
	}
	
	public static int djb2(String str) {
		int hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + ((hash << 5) - hash);
		}
		return hash;
	}

	public static String getFormatedDate() {
		Calendar c = Calendar.getInstance();
		return "[" + ((c.get(Calendar.MONTH)) + 1) + "/" + c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR) + "]";
	}

	public static int calculateKerning(byte[][] bytes_0, byte[][] bytes_1, int[] ints_2, byte[] bytes_3, int[] ints_4, int i_5, int i_6) {
		int i_8 = ints_2[i_5];
		int i_9 = i_8 + ints_4[i_5];
		int i_10 = ints_2[i_6];
		int i_11 = i_10 + ints_4[i_6];
		int i_12 = i_8;
		if (i_10 > i_8) {
			i_12 = i_10;
		}
		int i_13 = i_9;
		if (i_11 < i_9) {
			i_13 = i_11;
		}
		int i_14 = bytes_3[i_5] & 0xff;
		if ((bytes_3[i_6] & 0xff) < i_14) {
			i_14 = bytes_3[i_6] & 0xff;
		}
		byte[] bytes_15 = bytes_1[i_5];
		byte[] bytes_16 = bytes_0[i_6];
		int i_17 = i_12 - i_8;
		int i_18 = i_12 - i_10;
		for (int i_19 = i_12; i_19 < i_13; i_19++) {
			int i_20 = bytes_15[i_17++] + bytes_16[i_18++];
			if (i_20 < i_14) {
				i_14 = i_20;
			}
		}
		return -i_14;
	}

	public static int stringToInt(CharSequence charsequence_0) {
		int i_2 = charsequence_0.length();
		int i_3 = 0;
		for (int i_4 = 0; i_4 < i_2; i_4++) {
			i_3 = (i_3 << 5) - i_3 + charsequence_0.charAt(i_4);
		}
		return i_3;
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	public static boolean validEmail(String emailStr) {
		Matcher matcher = Utils.VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public static String concat(String[] args) {
		String str = "";
		for (int i = 0; i < args.length; i++)
			str += args[i] + ((i == args.length - 1) ? "" : " ");
		return str;
	}

	public static String concat(String[] args, int start) {
		String str = "";
		for (int i = start; i < args.length; i++)
			str += args[i] + ((i == args.length - 1) ? "" : " ");
		return str;
	}

	public static String addArticle(String name) {
		String s = "";
		switch(name.toCharArray()[0]) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
		case 'A':
		case 'E':
		case 'I':
		case 'O':
		case 'U':
			s += "an ";
			break;
			default:
				s += "a ";
				break;
		}
		s += name;
		return s;
	}

	public static int diff(int a, int b) {
		return Math.abs(a-b);
	}

	public static String removeInvalidChars(String s) {
		return s.replaceAll("[^\\sa-zA-Z0-9_]", "").replace("  ", " ");
	}

	public static int toInterfaceHash(int interfaceId, int componentId) {
		return (interfaceId << 16) + componentId;
	}

	public static String kmify(int num) {
		if (num < 0)
			return "";
		if (num < 10000)
			return ""+num;
		if (num < 10000000)
			return (num / 1000)+"K";
		return (num / 1000000)+"M";
	}
	
	public static Object[] streamObjects(Object... objects) {
		List<Object> list = new ArrayList<>();
		for (Object object : objects) {
			if (object.getClass().isArray()) {
				for (int i = 0; i < Array.getLength(object); i++) {
					list.add(Array.get(object, i));
				}
			} else
				list.add(object);
		}
		return list.toArray();
	}
}
