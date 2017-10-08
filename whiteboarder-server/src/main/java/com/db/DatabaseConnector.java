package com.db;

import com.core.Logger;
import com.model.*;
import java.sql.*;
import javax.sql.rowset.serial.SerialBlob;
import java.util.Date;

public class DatabaseConnector {

	// TODO: Validation?

	public static final String LOCAL_MYSQL_DB = "jdbc:mysql://localhost:3306/mysql";
	public static final String LOCAL_MYSQL_USER = "root";
	
	public static final String MYSQL_DB = "mysql.cs.iastate.edu:3306";
	public static final String MYSQL_USER = "dbu309ytc1";
	public static final String MYSQL_PASS = "sffwC#x#";

	private Connection c;

	public DatabaseConnector(boolean isLocal) {
		String host = isLocal ? LOCAL_MYSQL_DB : MYSQL_DB;
		String username = isLocal ? LOCAL_MYSQL_USER : MYSQL_USER;
		String password = isLocal ? null : MYSQL_PASS;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(host, username, password);
			if (c != null)
				Logger.log.info("Connected to MySQL database");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		initTables();
	}

	// Create Tables
	private void initTables() {
		try {
			Statement stmt = c.createStatement();
			stmt.addBatch(MySQL.CREATE_WHITEBOARDS_TABLE);
			stmt.addBatch(MySQL.CREATE_IMAGES_TABLE);
			stmt.addBatch(MySQL.CREATE_EDITS_TABLE);
			stmt.addBatch(MySQL.CREATE_USERS_TABLE);
			stmt.executeBatch();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void endConnection() {
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Mutations
	public void addWhiteboarderSession(Whiteboard wb) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_WHITEBOARD);
			stmt.setString(1, wb.getWbID());
			stmt.setString(2, wb.getName());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void removeWhiteboarderSession(Whiteboard wb) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_WHITEBOARD);
			stmt.setString(1, wb.getWbID());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void renameWhiteboarderSession(String wbID, String newName) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.RENAME_WHITEBOARD);
			stmt.setString(1, newName);
			stmt.setString(2, wbID);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addImage(Image img) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_IMAGE);
			stmt.setString(1, img.getWbID());
			stmt.setString(2, img.getFilename());
			stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
			if (img.getBytes() != null)
				stmt.setBlob(3, (Blob) new SerialBlob(img.getBytes()));
			else
				stmt.setNull(3, java.sql.Types.BLOB);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addEdit(Edit edit) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_EDIT);
			stmt.setString(1, edit.getWbID());
			stmt.setString(2, edit.getUsername());
			stmt.setInt(3, edit.getColor());
			stmt.setInt(4, edit.getBrushSize());
			stmt.setTimestamp(5, new Timestamp(edit.getTimestamp().getTime()));
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addBatchedEdits(Edit[] edits) {
		String sql = MySQL.ADD_EDIT;
		for (int i = 1; i < edits.length; i++)
			sql += ", (?, ?, ?, ?, ?)";

		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			for (int i = 0; i < edits.length; i++) {
				int j = i*5;
				Edit edit = edits[i];

				stmt.setString(j+1, edit.getWbID());
				stmt.setString(j+2, edit.getUsername());
				stmt.setInt(j+3, edit.getColor());
				stmt.setInt(j+4, edit.getBrushSize());
				stmt.setTimestamp(j+5, new Timestamp(edit.getTimestamp().getTime()));
			}
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void removeEdit(Edit edit) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_EDIT);
			stmt.setInt(1, edit.getEditID());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void removeBatchedEdits(Edit[] edits) {
		String sql = MySQL.REMOVE_BATCHED_EDITS;
		sql += " (";
		for (Edit e : edits) {
			sql += e.getEditID();
			sql += ", ";
		}
		sql = sql.substring(0, sql.length()-2) + ")";
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addUser(User user) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.ADD_USER);
			stmt.setString(1, user.getWbID());
			stmt.setString(2, user.getUsername());
			stmt.setInt(3, user.getMode().getValue());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void renameUser(User user, String newName) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_USER);
			stmt.setString(1, user.getWbID());
			stmt.setString(2, user.getUsername());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setUserMode(User user, Mode mode) {
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.SET_USER_MODE);
			stmt.setInt(1, user.getMode().getValue());
			stmt.setString(2, user.getWbID());
			stmt.setString(3, user.getUsername());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Queries
	public Whiteboard getWhiteboard(String wbID) {
		String name;
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_WHITEBOARD);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();

			name = rs.getString("Name");
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		Whiteboard wb = new Whiteboard(wbID, name);
		// TODO: Populate wb's Edits and Images
		return wb;
	}

	public Image getImage(String wbID) {
		String filename;
		byte[] bytes;
		try {
			PreparedStatement stmt = c.prepareStatement(MySQL.GET_IMAGE);
			stmt.setString(1, wbID);

			ResultSet rs = stmt.executeQuery();

			filename = rs.getString("Filename");
			Blob blob = rs.getBlob("Bytes");
			bytes = blob.getBytes(1, (int) blob.length());
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return new Image(wbID, filename, bytes);
	}
}