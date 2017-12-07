package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model class Whiteboard is a whiteboarder session.
 * @author Stephen Davidson
 */
public class Whiteboard {
	public static final int MAX_WB_NAME_LENGTH = 32;

	private String wbID;
	private String name;
	private List<Image> imgHistory;
	private List<Edit> edits;

	/**
	 * Class constructor.
	 * @param name name of this Whiteboard.
	 */
	public Whiteboard(String name) {
		this(UUID.randomUUID().toString(), name, new ArrayList<Image>(), new ArrayList<Edit>());
	}

	/**
	 * Class constructor.
	 * @param wbID id.
	 * @param name this Whiteboard's name.
	 * @param imgHistory a list of all the images uploaded to this Whiteboard.
	 * @param edits a list of all the edits applied to this Whiteboard.
	 */
	public Whiteboard(String wbID, String name, List<Image> imgHistory, List<Edit> edits) {
		this.wbID = wbID;
		this.name = name;
		this.imgHistory = (imgHistory != null) ? imgHistory : new ArrayList<Image>();
		this.edits = (edits != null) ? edits : new ArrayList<Edit>();
	}

	/**
	 * @return this Whiteboard's id.
	 */
	public String getWbID() {
		return this.wbID;
	}

	/**
	 * @return this Whiteboard's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param newName new name to be set.
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * @return the image most recently uploaded to this Whiteboard.
	 */
	public Image getCurrentImage() {
		if (this.imgHistory.size() == 0)
			return null;

		return this.imgHistory.get(0);
	}

	/**
	 * @return all the images uploaded to this Whiteboard.
	 */
	public List<Image> getImageHistory() {
		return this.imgHistory;
	}

	/**
	 * @param img image to be uploaded to this Whiteboard.
	 */
	public void addImage(Image img) {
		if (img == null)
			return;

		this.imgHistory.add(0, img);
	}

	/**
	 * @return all the edits applied on this Whiteboard.
	 */
	public List<Edit> getEdits() {
		return this.edits;
	}

	/**
	 * @param edits list of edits to be set.
	 */
	public void setEdits(List<Edit> edits) {
		this.edits = edits;
	}

	/**
	 * @param edit edit to be applied to this Whiteboard.
	 */
	public void addEdit(Edit edit) {
		this.edits.add(edit);
	}

	/**
	 * @param edit edit to be removed from this Whiteboard.
	 */
	public Edit removeEdit(Edit edit) {
		if (edit == null)
			return null;

		boolean removed = this.edits.remove(edit);
		return removed ? edit : null;
	}
}