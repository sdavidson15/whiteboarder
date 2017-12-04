package com.model;

import java.util.Date;
import java.util.List;

public class Edit {

	private int editID;
	private String wbID;
	private String username;
	private int color;
	private int brushSize;
	private List<Point> points;
	private Date timestamp;

	public Edit(int editID, String wbID, String username, int color, int brushSize, List<Point> points) {
		this.wbID = wbID;
		this.editID = editID;
		this.username = username;
		this.color = color;
		this.brushSize = brushSize;
		this.points = points;
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

	public List<Point> getPoints() {
		return this.points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
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