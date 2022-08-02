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
package com.rs.lib.util.reflect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.packets.decoders.ReflectionCheckResponse.ResponseCode;

public class ReflectionCheck {
	
	public enum Type {
		GET_INT,
		SET_INT,
		GET_FIELD_MODIFIERS,
		GET_METHOD_RETURN_VALUE,
		GET_METHOD_MODIFIERS
	}
	
	private Type type;
	private String className;
	private String modifiers;
	private String returnType;
	private String methodName;
	private String[] paramTypes;
	private Object[] paramValues;
	private int fieldValue;
	private ReflectionResponse response;
	
	public ReflectionCheck(String className, String methodName, boolean field) {
		this.type = field ? Type.GET_FIELD_MODIFIERS : Type.GET_METHOD_MODIFIERS;
		this.className = className;
		this.methodName = methodName;
	}
	
	public ReflectionCheck(String className, String methodName, int fieldValue) {
		this.type = Type.SET_INT;
		this.className = className;
		this.methodName = methodName;
		this.fieldValue = fieldValue;
	}
	
	public ReflectionCheck(String className, String modifiers, String returnType, String methodName, String[] paramTypes) {
		this.type = Type.GET_METHOD_MODIFIERS;
		this.className = className;
		this.modifiers = modifiers;
		this.returnType = returnType;
		this.methodName = methodName;
		this.paramTypes = paramTypes;
	}
	
	public ReflectionCheck(String className, String modifiers, String returnType, String methodName, Object[] paramValues) {
		this.type = Type.GET_METHOD_RETURN_VALUE;
		this.className = className;
		this.modifiers = modifiers;
		this.returnType = returnType;
		this.methodName = methodName;
		this.paramTypes = new String[paramValues.length];
		for (int i = 0;i < paramTypes.length;i++) {
			if (paramValues[i] instanceof Byte)
				paramTypes[i] = "B";
			else if (paramValues[i] instanceof Integer)
				paramTypes[i] = "I";
			else if (paramValues[i] instanceof Short)
				paramTypes[i] = "S";
			else if (paramValues[i] instanceof Long)
				paramTypes[i] = "J";
			else if (paramValues[i] instanceof Boolean)
				paramTypes[i] = "Z";
			else if (paramValues[i] instanceof Float)
				paramTypes[i] = "F";
			else if (paramValues[i] instanceof Double)
				paramTypes[i] = "D";
			else if (paramValues[i] instanceof Character)
				paramTypes[i] = "C";
			else if (paramValues[i] instanceof Void)
				paramTypes[i] = "void";
			else
				paramTypes[i] = paramValues[i].getClass().getName();
		}
		this.paramValues = paramValues;
	}
	
	public String getClassName() {
		return className;
	}
	public String getReturnType() {
		return returnType;
	}
	public String getMethodName() {
		return methodName;
	}
	public String[] getParamTypes() {
		return paramTypes;
	}

	public String getModifiers() {
		return modifiers;
	}

	public Type getType() {
		return type;
	}
	
	public void decode(InputStream stream) {
		ResponseCode code = ResponseCode.forId(stream.readByte());
		response = new ReflectionResponse(code);
		switch(type) {
		case GET_INT, GET_FIELD_MODIFIERS, GET_METHOD_MODIFIERS -> {
			if (code == ResponseCode.SUCCESS)
				response.setData(stream.readInt());
		}
		case GET_METHOD_RETURN_VALUE -> {
			switch(code) {
			case NUMBER -> response.setData(stream.readLong());
			case STRING -> response.setStringData(stream.readString());
			default -> {}
			}
		}
		case SET_INT -> {}
		}
	}

	public void encode(OutputStream stream) {
		stream.writeByte(type.ordinal());
		switch(type) {
		case GET_INT, SET_INT, GET_FIELD_MODIFIERS -> {
			stream.writeString(className);
			stream.writeString(methodName);
			if (type == Type.SET_INT)
				stream.writeInt(fieldValue);
		}
		case GET_METHOD_RETURN_VALUE, GET_METHOD_MODIFIERS -> {
			stream.writeString(className);
			stream.writeString(methodName);
			stream.writeByte(paramTypes.length);
			for (String param : paramTypes)
				stream.writeString(param);
			stream.writeString(returnType);
			if (type == Type.GET_METHOD_RETURN_VALUE) {
				for (Object obj : paramValues) {
					try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
						oos.writeObject(obj);
						byte[] data = bos.toByteArray();
						stream.writeInt(data.length);
						stream.writeBytes(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		}
	}
}
