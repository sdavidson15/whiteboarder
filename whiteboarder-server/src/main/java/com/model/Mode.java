package com.model;

public enum Mode {
	HOST(0), COLLABORATOR(1), VIEWER(2);

	private final int value;

	private Mode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}