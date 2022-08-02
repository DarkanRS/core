package com.rs.lib.util.reflect;

import com.rs.lib.net.packets.decoders.ReflectionCheckResponse.ResponseCode;

public class ReflectionResponse {
	private ResponseCode code;
	private long data;
	private String stringData;
	
	public ReflectionResponse(ResponseCode code) {
		this.code = code;
	}

	public ResponseCode getCode() {
		return code;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getStringData() {
		return stringData;
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
	}
}
