package com.model;

public class User {
	public static final int MAX_USER_NAME_LENGTH = 32;

	private String wbID;
	private String username;
	private Mode mode;

	public User(String wbID, String username, Mode mode) {
		this.wbID = wbID;
		this.username = username;
		this.mode = mode;
	}

	public String getWbID() {
		return this.wbID;
	}

	public String getUsername() {
		return this.username;
	}

	public void setName(String newName) {
		this.username = newName;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setMode(Mode newMode) {
		this.mode = newMode;
	}
}