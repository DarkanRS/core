package com.rs.lib.web;

public class APIResponse<T> {

	private String body;
	private T data;
	
	public APIResponse(String body, T data) {
		this.body = body;
		this.data = data;
	}
	
	public String getBody() {
		return body;
	}
	
	public T getData() {
		return data;
	}
}
