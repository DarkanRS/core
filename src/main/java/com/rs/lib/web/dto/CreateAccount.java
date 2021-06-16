package com.rs.lib.web.dto;

public class CreateAccount {

	private String username;
	private String password;
	private String email;
	
	public CreateAccount(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
}
