package com.rs.lib.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rs.lib.net.packets.PacketEncoder;

public class PacketEncoderAdapter implements JsonSerializer<PacketEncoder>, JsonDeserializer<PacketEncoder> {

	private static final String CLASS_KEY = "1_CMK";

	@Override
	public PacketEncoder deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObj = jsonElement.getAsJsonObject();
		String className = jsonObj.get(CLASS_KEY).getAsString();
		try {
			Class<?> clz = Class.forName(className);
			return jsonDeserializationContext.deserialize(jsonElement, clz);
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(PacketEncoder object, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonElement jsonEle = jsonSerializationContext.serialize(object, object.getClass());
		jsonEle.getAsJsonObject().addProperty(CLASS_KEY, object.getClass().getCanonicalName());
		return jsonEle;
	}

}