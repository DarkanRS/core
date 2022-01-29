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
