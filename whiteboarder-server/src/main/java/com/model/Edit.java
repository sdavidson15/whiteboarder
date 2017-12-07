package com.model;

import java.util.Date;
import java.util.List;

/**
 * Model class Edit is an edit drawn on a Whiteboard.
 * @author Stephen Davidson
 */
public class Edit {

	private int editID;
	private String wbID;
	private String username;
	private int color;
	private int brushSize;
	private List<Point> points;
	private Date timestamp;

	/**
	 * Class constructor.
	 * @param editID id.
	 * @param wbID id of this edit's whiteboarder session.
	 * @param username name of the user who applied this edit.
	 * @param color brush color.
	 * @param brushSize brush size.
	 * @param points list of the points that compose this edit.
	 */
	public Edit(int editID, String wbID, String username, int color, int brushSize, List<Point> points) {
		this.wbID = wbID;
		this.editID = editID;
		this.username = username;
		this.color = color;
		this.brushSize = brushSize;
		this.points = points;
		this.timestamp = new Date();
	}

	/**
	 * @return this edit's id.
	 */
	public int getEditID() {
		return this.editID;
	}

	/**
	 * @param editID edit id to be set.
	 */
	public void setEditID(int editID) {
		this.editID = editID;
	}

	/**
	 * @return this edit's whiteboarder session id.
	 */
	public String getWbID() {
		return this.wbID;
	}

	/**
	 * @return the name of the user who applied this edit.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @return this edit's brush color.
	 */
	public int getColor() {
		return this.color;
	}

	/**
	 * @return this edit's brush size.
	 */
	public int getBrushSize() {
		return this.brushSize;
	}

	/**
	 * @return the list of points that compose this edit.
	 */
	public List<Point> getPoints() {
		return this.points;
	}

	/**
	 * @param points the list of points to be set.
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
	}

	/**
	 * @return the time this edit was applied.
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * setNewTimestamp sets this edit's timestamp to the current time.
	 */
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