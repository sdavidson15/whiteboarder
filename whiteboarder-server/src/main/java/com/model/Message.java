package com.model;

import java.util.Date;

public class Message {
	public static final int MAX_MSG_LENGTH = 200;

	private int messageID;
	private String wbID;
	private String username;
	private String msg;
	private Date timestamp;

	public Message(int messageID, String wbID, String username, String msg) {
		this.wbID = wbID;
		this.messageID = messageID;
		this.username = username;
		this.msg = msg;
		this.timestamp = new Date();
	}

	public int getMessageID() {
		return this.messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public String getWbID() {
		return this.wbID;
	}

	public String getUsername() {
		return this.username;
	}

	public String getMessage() {
		return this.msg;
	}

	public void setMessage() {
		this.msg = msg;
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

		Message other = (Message) o;
		return (this.timestamp == null && other.getTimestamp() == null)
				|| (this.timestamp != null && this.timestamp.equals((other.getTimestamp())))
						&& (this.username == null && other.getUsername() == null)
				|| (this.username != null && this.username.equals((other.getUsername())))
						&& (this.wbID == null && other.getWbID() == null)
				|| (this.wbID != null && this.wbID.equals((other.getWbID())));
	}
}