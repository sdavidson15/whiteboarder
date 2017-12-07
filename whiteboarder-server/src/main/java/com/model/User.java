package com.model;

/**
 * Model class User.
 * @author Stephen Davidson
 */
public class User {
	public static final int MAX_USER_NAME_LENGTH = 32;

	private String wbID;
	private String username;
	private Mode mode;

	/**
	 * Class constructor.
	 * @param wbID id of this User's whiteboarder session.
	 * @param username name of this user.
	 * @param mode this User's mode (host, collaborator, or viewer).
	 */
	public User(String wbID, String username, Mode mode) {
		this.wbID = wbID;
		this.username = username;
		this.mode = mode;
	}

	/**
	 * @return this user's whiteboarder session id.
	 */
	public String getWbID() {
		return this.wbID;
	}

	/**
	 * @return this user's name.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param newName new name to be set.
	 */
	public void setName(String newName) {
		this.username = newName;
	}

	/**
	 * @return this user's mode.
	 */
	public Mode getMode() {
		return this.mode;
	}

	/**
	 * @param newMode new mode to be set.
	 */
	public void setMode(Mode newMode) {
		this.mode = newMode;
	}
}