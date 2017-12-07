package com.model;

import com.core.Logger;
import java.util.Date;
import java.util.UUID;

/**
 * Model class Image is the background image of a Whiteboard.
 * @author Stephen Davidson
 */
public class Image {

	public static final int MAX_IMG_NAME_LENGTH = 32;

	private int imgID;
	private String wbID;
	private String filename;
	private byte[] bytes;
	private Date timestamp;

	/**
	 *  Class constructor.
	 * @param wbID id of this image's whiteboarder session.
	 * @param filename original file name of this image.
	 * @param bytes binary data for this image.
	 **/
	public Image(String wbID, String filename, byte[] bytes) {
		this(-1, wbID, filename, bytes, new Date());
	}

	/**
	 *  Class constructor.
	 * @param imgID id.
	 * @param wbID id of this image's whiteboarder session.
	 * @param filename original file name of this image.
	 * @param bytes bytes that compose this image.
	 * @param timestamp time that this image was uploaded.
	 **/
	public Image(int imgID, String wbID, String filename, byte[] bytes, Date timestamp) {
		this.imgID = imgID;
		this.wbID = wbID;
		this.filename = filename;
		this.bytes = bytes;
		this.timestamp = timestamp;
	}

	/**
	 * @return this image's id.
	 */
	public int getImgID() {
		return this.imgID;
	}

	/**
	 * @param imgID image id to be set.
	 */
	public void setImgID(int imgID) {
		if (isPersisted()) {
			Logger.log.warning("Cannot change the imgID on a persisted Image.");
			return;
		}
		this.imgID = imgID;
	}

	/**
	 * @return this image's whiteboarder session id.
	 */
	public String getWbID() {
		return this.wbID;
	}

	/**
	 * @return the original filename of this image.
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * @return the bytes that compose this image.
	 */
	public byte[] getBytes() {
		return this.bytes;
	}

	/**
	 * @param bytes bytes to be set as this image's bytes.
	 */
	public void setBytes(byte[] bytes) {
		if (isPersisted()) {
			Logger.log.warning("Cannot set the bytes on a persisted Image.");
			return;
		}
		this.bytes = bytes;
	}

	/**
	 * @return the time that this image was uploaded.
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp the timestamp to be set as this image's upload timestamp.
	 */
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
