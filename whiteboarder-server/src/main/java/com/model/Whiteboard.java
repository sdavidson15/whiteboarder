package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Whiteboard {
	public static final int MAX_WB_NAME_LENGTH = 32;

	private String wbID;
	private String name;
	private List<Image> imgHistory;
	private List<Edit> edits;

	/**
	 *  General purpose Whiteboard contructor
	 **/
	public Whiteboard(String name) {
		this(UUID.randomUUID().toString(), name, new ArrayList<Image>(), new ArrayList<Edit>());
	}

	/**
	 * Constructor used only for loading an existing Whiteboard from the database
	 **/
	public Whiteboard(String wbID, String name, List<Image> imgHistory, List<Edit> edits) {
		this.wbID = wbID;
		this.name = name;
		this.imgHistory = (imgHistory != null) ? imgHistory : new ArrayList<Image>();
		this.edits = (edits != null) ? edits : new ArrayList<Edit>();
	}

	public String getWbID() {
		return this.wbID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public Image getCurrentImage() {
		if (this.imgHistory.size() == 0)
			return null;

		return this.imgHistory.get(0);
	}

	public List<Image> getImageHistory() {
		return this.imgHistory;
	}

	public void addImage(Image img) {
		if (img == null)
			return;

		this.imgHistory.add(0, img);
	}

	public List<Edit> getEdits() {
		return this.edits;
	}

	public void setEdits(List<Edit> edits) {
		this.edits = edits;
	}

	public void addEdit(Edit edit) {
		this.edits.add(edit);
	}

	public Edit removeEdit(Edit edit) {
		if (edit == null)
			return null;

		boolean removed = this.edits.remove(edit);
		return removed ? edit : null;
	}
}