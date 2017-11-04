package com.db;

import com.core.Context;
import com.core.Logger;
import com.core.WbException;
import com.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.rowset.serial.SerialBlob;


public class DatabaseConnector {

	public static final String LOCAL_MYSQL_DB = "jdbc:mysql://localhost:3306/mysql";
	public static final String LOCAL_MYSQL_USER = "root";
	public static final String LOCAL_MYSQL_PASS = null;

	public static final String MYSQL_DB = "mysql.cs.iastate.edu:3306";
	public static final String MYSQL_USER = "dbu309ytc1";
	public static final String MYSQL_PASS = "sffwC#x#";

	private String host;
	private String username;
	private String password;
	private boolean isLocal;

	private Connection c;

	public DatabaseConnector(boolean isLocal) {
		this.host = isLocal ? LOCAL_MYSQL_DB : MYSQL_DB;
		this.username = isLocal ? LOCAL_MYSQL_USER : MYSQL_USER;
		this.password = isLocal ? LOCAL_MYSQL_PASS : MYSQL_PASS;
		this.isLocal = isLocal;
	}

	public void startConnection() throws WbException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(this.host, this.username, this.password);
		} catch (Exception e) {
			throw new WbException(WbException.DB_START_CONNECTION, e);
		}
		if (c != null) {
			Logger.log.info("Connected to MySQL database.");
			initTables(this.isLocal);
		}
	}

	public void endConnection() throws WbException {
		try {
			c.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_END_CONNECTION, e);
		}
	}

	// Create Tables
	private void initTables(boolean isLocal) throws WbException {
		if (isLocal) {
			try {
				Statement stmt = c.createStatement();
				stmt.addBatch(MySQL.REMOVE_WHITEBOARDS_TABLE);
				stmt.addBatch(MySQL.REMOVE_IMAGES_TABLE);
				stmt.addBatch(MySQL.REMOVE_EDITS_TABLE);
				stmt.addBatch(MySQL.REMOVE_USERS_TABLE);
				stmt.executeBatch();
				stmt.close();
			} catch (Exception e) {
				throw new WbException(WbException.DB_CLEAR_TABLES, e);
			}
		}

		try {
			Statement stmt = c.createStatement();
			stmt.addBatch(MySQL.CREATE_WHITEBOARDS_TABLE);
			stmt.addBatch(MySQL.CREATE_IMAGES_TABLE);
			stmt.addBatch(MySQL.CREATE_EDITS_TABLE);
			stmt.addBatch(MySQL.CREATE_USERS_TABLE);
			stmt.executeBatch();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_INIT_TABLES, e);
		}
	}

	// Mutations
	public void addWhiteboard(Whiteboard wb) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_WHITEBOARD);
			stmt.setString(1, wb.getWbID());
			stmt.setString(2, wb.getName());
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_ADD_WB, e);
		}
	}

	public void removeWhiteboard(Whiteboard wb) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_WHITEBOARD);
			stmt.setString(1, wb.getWbID());
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for Whiteboard does not exist
			throw new WbException(WbException.DB_REMOVE_WB, e);
		}
	}

	public void renameWhiteboard(String wbID, String newName) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.RENAME_WHITEBOARD);
			stmt.setString(1, newName);
			stmt.setString(2, wbID);
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for Whiteboard does not exist
			throw new WbException(WbException.DB_RENAME_WB, e);
		}
	}

	public Image addImage(Image img) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_IMAGE, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, img.getWbID());
			stmt.setString(2, img.getFilename());
			stmt.setTimestamp(4, new Timestamp(img.getTimestamp().getTime()));
			if (img.getBytes() != null && img.getBytes().length > 0)
				stmt.setBlob(3, (Blob) new SerialBlob(img.getBytes()));
			else
				stmt.setNull(3, java.sql.Types.BLOB);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			img.setImgID(rs.getInt(1));
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for Whiteboard does not exist
			throw new WbException(WbException.DB_ADD_IMG, e);
		}

		return img;
	}

	public Edit addEdit(Edit edit) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_EDIT);
			stmt.setString(1, edit.getWbID());
			stmt.setString(2, edit.getUsername());
			stmt.setInt(3, edit.getColor());
			stmt.setInt(4, edit.getBrushSize());
			stmt.setTimestamp(5, new Timestamp(edit.getTimestamp().getTime()));
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for Whiteboard does not exist
			throw new WbException(WbException.DB_ADD_EDIT, e);
		}

		// TODO: Retrieve the edit ID
		return edit;
	}

	public void removeEdit(Edit edit) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_EDIT);
			stmt.setInt(1, edit.getEditID());
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for Whiteboard does not exist
			throw new WbException(WbException.DB_REMOVE_EDIT, e);
		}
	}

	public void addUser(User user) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_USER);
			stmt.setString(1, user.getWbID());
			stmt.setString(2, user.getUsername());
			stmt.setInt(3, user.getMode().getValue());
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_ADD_USER, e);
		}
	}

	public void renameUser(User user, String newName) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_USER);
			stmt.setString(1, user.getWbID());
			stmt.setString(2, user.getUsername());
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for User does not exist
			throw new WbException(WbException.DB_RENAME_USER, e);
		}
	}

	public void setUserMode(User user, Mode mode) throws WbException {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.SET_USER_MODE);
			stmt.setInt(1, user.getMode().getValue());
			stmt.setString(2, user.getWbID());
			stmt.setString(3, user.getUsername());
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			// TODO: Check for User does not exist
			throw new WbException(WbException.DB_SET_USER_MODE, e);
		}
	}

	// Queries
	public Whiteboard getWhiteboard(String wbID) throws WbException {
		String name = null;
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_WHITEBOARD);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				name = rs.getString("Name");
			rs.close();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_GET_WB, e);
		}
		// TODO: Populate wb's Edits and Images
		Whiteboard wb = new Whiteboard(wbID, name, null, null);
		return wb;
	}

	public Image getImage(String wbID) throws WbException {
		int imgID = -1;
		String filename = null;
		byte[] bytes = null;
		Date timestamp = null;

		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_IMAGE);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				imgID = rs.getInt("ImageID");
				filename = rs.getString("Filename");
				timestamp = rs.getTimestamp("Timestamp");

				Blob blob = rs.getBlob("Bytes");
				if (blob != null)
					bytes = blob.getBytes(1, (int) blob.length());
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_GET_IMG, e);
		}

		return new Image(imgID, wbID, filename, bytes, timestamp);
	}

	public List<Image> getImages(String wbID) throws WbException {
		ArrayList<Image> images = new ArrayList<Image>();
		
		int imgID = -1;
		String filename = null;
		byte[] bytes = null;
		Date timestamp = null;

		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_IMAGES);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				imgID = rs.getInt("ImageID");
				filename = rs.getString("Filename");
				timestamp = rs.getTimestamp("Timestamp");

				Blob blob = rs.getBlob("Bytes");
				// casted to an int from a long (problem when posting massive pictures?? over 4.2Gb i think)
				if (blob != null) bytes = blob.getBytes(1, (int) blob.length());

				images.add(new Image(imgID, wbID, filename, bytes, timestamp));
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_GET_IMGS, e);
		}

		return images;
	}

	public Set<Edit> getEdits(String wbID) throws WbException {
		HashSet<Edit> edits = new HashSet<Edit>();

		// WhiteboardID, Username, Color, BrushSize, Timestamp
		String username = null;
		int color = -1, brushsize = -1;
		Date timestamp = null;

		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_EDITS);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				username = rs.getString("Username");
				color = rs.getInt("Color");
				brushsize = rs.getInt("BrushSize");
				timestamp = rs.getTimestamp("Timestamp");

				edits.add(new Edit(wbID, username, color, brushsize, null));
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			throw new WbException(WbException.DB_GET_EDITS, e);
		}

		return edits;
	}

	public User getUser(String wbID, String username) {
		int modeNum = -1;

		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_USER);
			stmt.setString(1, wbID);
			stmt.setString(2, username);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				modeNum = rs.getInt("Mode");
				// TODO: add these for other values? (Possible -1/null color, bytesize, timestamp, etc.)
			}
		} catch (Exception e) {
			throw new WbException(WbException.DB_GET_USER, e);
		}

		switch (modeNum) {
			case 0:
				return new User(wbID, username, Mode.HOST);
			case 1:
				return new User(wbID, username, Mode.COLLABORATOR);
			case 2:
				return new User(wbID, username, Mode.VIEWER);
			default:
				throw new WbException(WbException.DB_INVALID_MODE);
		}
	}

	public Set<User> getUsers(String wbID) {
		HashSet<User> users = new HashSet<User>();

		String username = null;
		int modeNum = -1;

		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_USERS);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				username = rs.getString("Username");
				modeNum = rs.getInt("Mode");

				switch (modeNum) {
					case 0:
						users.add(new User(wbID, username, Mode.HOST));
						break;
					case 1:
						users.add(new User(wbID, username, Mode.COLLABORATOR));
						break;
					case 2:
						users.add(new User(wbID, username, Mode.VIEWER));
						break;
					default:
						throw new WbException(WbException.DB_INVALID_MODE);
				}
			}
		} catch (Exception e) {
			throw new WbException(WbException.DB_GET_USERS, e);
		}

		return users;
	}
}