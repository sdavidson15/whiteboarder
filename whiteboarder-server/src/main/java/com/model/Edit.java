package com.model;

import java.awt.Point;
import java.util.Date;
import java.util.Set;

public class Edit {

	private int editID;
	private String wbID;
	private String username;
	private int color;
	private int brushSize;
	private Set<Point> points;
	private Date timestamp;

	public Edit(int editID, String wbID, String username, int color, int brushSize, Set<Point> points) {
		this.wbID = wbID;
		this.editID = editID;
		this.username = username;
		this.color = color;
		this.brushSize = brushSize;
		this.points = points;

		this.editID = -1;
		this.timestamp = new Date();
	}

	public int getEditID() {
		return this.editID;
	}

	public void setEditID(int editID) {
		this.editID = editID;
	}

	public String getWbID() {
		return this.wbID;
	}

	public String getUsername() {
		return this.username;
	}

	public int getColor() {
		return this.color;
	}

	public int getBrushSize() {
		return this.brushSize;
	}

	public Set<Point> getPoints() {
		return this.points;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setNewTimestamp() {
		this.timestamp = new Date();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}

		Edit other = (Edit) o;
		return (this.timestamp == null && other.getTimestamp() == null)
				|| (this.timestamp != null && this.timestamp.equals((other.getTimestamp())))
						&& (this.username == null && other.getUsername() == null)
				|| (this.username != null && this.username.equals((other.getUsername())))
						&& (this.wbID == null && other.getWbID() == null)
				|| (this.wbID != null && this.wbID.equals((other.getWbID())));
	}
}