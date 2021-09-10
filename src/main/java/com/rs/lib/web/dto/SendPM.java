package com.rs.lib.web.dto;

public class SendPM {
	private String fromDisplayName;
	private String toDisplayName;
	private String message;
	
	public SendPM(String fromDisplayName, String toDisplayName, String message) {
		this.fromDisplayName = fromDisplayName;
		this.toDisplayName = toDisplayName;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getFromDisplayName() {
		return fromDisplayName;
	}

	public String getToDisplayName() {
		return toDisplayName;
	}
}
