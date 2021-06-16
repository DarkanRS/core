package com.rs.lib.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateAdapter implements JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String date = element.getAsString();

		SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, YYYY, h:mm:ss a");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, YYYY h:mm:ss a");

		try {
			return format1.parse(date);
		} catch (ParseException exp) {
			try {
				return format2.parse(date);
			} catch (ParseException exp2) {
				System.err.println("Failed to parse Date:" + exp2);
				return null;
			}
		}
	}
}
