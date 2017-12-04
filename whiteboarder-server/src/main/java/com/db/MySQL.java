package com.db;

import com.model.Image;
import com.model.Message;
import com.model.User;
import com.model.Whiteboard;


public class MySQL {
	// Create Tables
	final static String CREATE_WHITEBOARDS_TABLE = "CREATE TABLE IF NOT EXISTS Whiteboards ("
			+ "WhiteboardID CHAR(36) NOT NULL, Name VARCHAR(" + Whiteboard.MAX_WB_NAME_LENGTH + "), "
			+ "PRIMARY KEY(WhiteboardID));";
	final static String CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS Images (ImageID INT AUTO_INCREMENT, "
			+ "WhiteboardID CHAR(36) NOT NULL, Filename VARCHAR(" + Image.MAX_IMG_NAME_LENGTH + "), "
			+ "Bytes MEDIUMBLOB, Timestamp TIMESTAMP NOT NULL, PRIMARY KEY(ImageID));";
	final static String CREATE_EDITS_TABLE = "CREATE TABLE IF NOT EXISTS Edits (EditID INT AUTO_INCREMENT, "
			+ "WhiteboardID CHAR(36) NOT NULL, Username VARCHAR(" + User.MAX_USER_NAME_LENGTH + "), "
			+ "Color INT NOT NULL, BrushSize INT NOT NULL, Timestamp TIMESTAMP NOT NULL, " + "PRIMARY KEY (EditID));";
	final static String CREATE_POINTS_TABLE = "CREATE TABLE IF NOT EXISTS Points (EditID INT, X INT, Y INT);";
	final static String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users (WhiteboardID CHAR(36) NOT NULL, "
			+ "Username VARCHAR(" + User.MAX_USER_NAME_LENGTH + ") NOT NULL,Mode TINYINT NOT NULL, "
			+ "PRIMARY KEY (WhiteboardID,Username));";
	final static String CREATE_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS Messages (MessageID INT AUTO_INCREMENT, "
			+ "WhiteboardID CHAR(36) NOT NULL, Username VARCHAR(" + User.MAX_USER_NAME_LENGTH + "), "
			+ "Message VARCHAR(" + Message.MAX_MSG_LENGTH + "), Timestamp TIMESTAMP NOT NULL, " + "PRIMARY KEY (MessageID));";

	// Remove Tables
	final static String REMOVE_WHITEBOARDS_TABLE = "DROP TABLE IF EXISTS Whiteboards;";
	final static String REMOVE_IMAGES_TABLE = "DROP TABLE IF EXISTS Images;";
	final static String REMOVE_EDITS_TABLE = "DROP TABLE IF EXISTS Edits;";
	final static String REMOVE_POINTS_TABLE = "DROP TABLE IF EXISTS Points;";
	final static String REMOVE_USERS_TABLE = "DROP TABLE IF EXISTS Users;";
	final static String REMOVE_MESSAGES_TABLE = "DROP TABLE IF EXISTS Messages;";

	// Mutations
	final static String ADD_WHITEBOARD = "INSERT INTO Whiteboards (WhiteboardID, Name) VALUES (?, ?);";
	final static String REMOVE_WHITEBOARD = "DELETE FROM Whiteboards WHERE WhiteboardID = ?;";
	final static String RENAME_WHITEBOARD = "UPDATE Whiteboards SET Name = ? WHERE WhiteboardID = ?;";
	final static String ADD_IMAGE = "INSERT INTO Images (WhiteboardID, Filename, Bytes, Timestamp) VALUES (?, ?, ?, ?);";
	final static String ADD_EDIT = "INSERT INTO Edits (WhiteboardID, Username, Color, BrushSize, Timestamp) VALUES (?, ?, ?, ?, ?);";
	final static String REMOVE_EDIT = "DELETE FROM Edits WHERE EditID = ?;";
	final static String ADD_POINT = "INSERT INTO Points (EditID, X, Y) VALUES (?, ?, ?);";
	final static String REMOVE_POINTS = "DELETE FROM Points WHERE EditID = ?;";
	final static String ADD_USER = "INSERT INTO Users (WhiteboardID, Username, Mode) VALUES (?, ?, ?);";
	final static String REMOVE_USER = "DELETE FROM Users WHERE WhiteboardID = ? AND Username = ?;";
	final static String RENAME_USER = "UPDATE Users SET Username = ? WHERE WhiteboardID = ? AND Username = ?;";
	final static String SET_USER_MODE = "UPDATE Users SET Mode = ? WHERE WhiteboardID = ? AND Username = ?;";
	final static String ADD_MESSAGE = "INSERT INTO Messages (WhiteboardID, Username, Message, Timestamp) VALUES (?, ?, ?, ?);";
	final static String REMOVE_MESSAGE = "DELETE FROM Messages WHERE MessageID = ?;";

	// Queries
	final static String GET_WHITEBOARD = "SELECT WhiteboardID, Name FROM Whiteboards WHERE WhiteboardID = ?;";
	final static String GET_IMAGE = "SELECT ImageID, WhiteboardID, Filename, Bytes, Timestamp FROM Images WHERE WhiteboardID = ? ORDER BY ImageID DESC LIMIT 1;";
	final static String GET_IMAGES = "SELECT ImageID, WhiteboardID, Filename, Bytes, Timestamp FROM Images WHERE WhiteboardID = ? ORDER BY ImageID DESC;";
	final static String GET_EDITS = "SELECT EditID, WhiteboardID, Username, Color, BrushSize, Timestamp FROM Edits WHERE WhiteboardID = ?;";
	final static String GET_POINTS = "SELECT EditID, X, Y FROM Points WHERE EditID = ?;";
	final static String GET_USER = "SELECT WhiteboardID, Username, Mode FROM Users WHERE WhiteboardID = ? AND Username = ?;";
	final static String GET_USERS = "SELECT WhiteboardID, Username, Mode FROM Users WHERE WhiteboardID = ?;";
	final static String GET_MESSAGES = "SELECT MessageID, WhiteboardID, Username, Message, Timestamp FROM Messages WHERE WhiteboardID = ? ORDER BY MessageID DESC;";
}