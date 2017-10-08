package com.model;

import java.util.Date;
import java.util.UUID;

public class Image {

	public static final int MAX_IMG_NAME_LENGTH = 32;

	private int imgID;
	private String wbID;
	private String filename;
	private byte[] bytes;
	private Date timestamp;

	public Image(String wbID, String filename, byte[] bytes) {
		this.wbID = wbID;
		this.filename = filename;
		this.bytes = bytes;

		this.imgID = -1;
		this.timestamp = new Date();
	}

	public int getImgID() {
		return this.imgID;
	}
	public void setImgID(int imgID) {
		this.imgID = imgID;
	}

	public String getWbID() {
		return this.wbID;
	}
	// Remove this after Demo 2
	public void setWbID(String wbID) {
		this.wbID = wbID;
	}

	public String getFilename() {
		return this.filename;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}
}