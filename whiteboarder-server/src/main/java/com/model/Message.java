package com.model;

import java.util.Date;

/**
 * Model class Message is a message sent between users in a whiteboarder session.
 * @author Jared Gorton
 */
public class Message {
	public static final int MAX_MSG_LENGTH = 200;

	private int messageID;
	private String wbID;
	private String username;
	private String msg;
	private Date timestamp;

	/**
	 * Class constructor.
	 * @param messageID id.
	 * @param wbID id of this message's whiteboarder session.
	 * @param username name of the user who sent this message.
	 * @param msg message text.
	 */
	public Message(int messageID, String wbID, String username, String msg) {
		this.wbID = wbID;
		this.messageID = messageID;
		this.username = username;
		this.msg = msg;
		this.timestamp = new Date();
	}

	/**
	 * @return this messge's id.
	 */
	public int getMessageID() {
		return this.messageID;
	}

	/**
	 * @param messageID message id to be set.
	 */
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	/**
	 * @return this message's whiteboarder session id.
	 */
	public String getWbID() {
		return this.wbID;
	}

	/**
	 * @return the name of the user who sent this message.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @return this message's message text.
	 */
	public String getMessage() {
		return this.msg;
	}

	/**
	 * @return the time that this message was sent.
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * setNewTimestamp sets this message's timestamp to the current time.
	 */
	public void setNewTimestamp() {
		this.timestamp = new Date();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}

		Message other = (Message) o;
		return (this.timestamp == null && other.getTimestamp() == null)
				|| (this.timestamp != null && this.timestamp.equals((other.getTimestamp())))
						&& (this.username == null && other.getUsername() == null)
				|| (this.username != null && this.username.equals((other.getUsername())))
						&& (this.wbID == null && other.getWbID() == null)
				|| (this.wbID != null && this.wbID.equals((other.getWbID())));
	}
}