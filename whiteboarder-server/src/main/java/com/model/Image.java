package com.model;

import com.core.Logger;
import java.util.Date;
import java.util.UUID;

public class Image {

	public static final int MAX_IMG_NAME_LENGTH = 32;

	private int imgID;
	private String wbID;
	private String filename;
	private byte[] bytes;
	private Date timestamp;

	/**
	 *  General purpose Image constructor
	 **/
	public Image(String wbID, String filename, byte[] bytes) {
		this(-1, wbID, filename, bytes, new Date());
	}

	/** 
	 * Constructor used only for loading an existing Image from the database
	 **/
	public Image(int imgID, String wbID, String filename, byte[] bytes, Date timestamp) {
		this.imgID = imgID;
		this.wbID = wbID;
		this.filename = filename;
		this.bytes = bytes;
		this.timestamp = timestamp;
	}

	public int getImgID() {
		return this.imgID;
	}

	public void setImgID(int imgID) {
		if (isPersisted()) {
			Logger.log.warning("Cannot change the imgID on a persisted Image.");
			return;
		}
		this.imgID = imgID;
	}

	public String getWbID() {
		return this.wbID;
	}

	public String getFilename() {
		return this.filename;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public void setBytes(byte[] bytes) {
		if (isPersisted()) {
			Logger.log.warning("Cannot set the bytes on a persisted Image.");
			return;
		}
		this.bytes = bytes;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		if (isPersisted()) {
			Logger.log.warning("Cannot change the timestamp on a persisted Image.");
			return;
		}
		this.timestamp = timestamp;
	}

	private boolean isPersisted() {
		return this.imgID > 0;
	}
}
