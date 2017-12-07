package com.model;

/**
 * Enum class Mode lists the different Modes a User can be while in a whiteboarder session.
 */
public enum Mode {
	HOST(0), COLLABORATOR(1), VIEWER(2);

	private final int value;

	private Mode(int value) {
		this.value = value;
	}

	/**
	 * @return the integer representation of this Mode: HOST(0), COLLABORATOR(1), or VIEWER(2)
	 */
	public int getValue() {
		return value;
	}
}