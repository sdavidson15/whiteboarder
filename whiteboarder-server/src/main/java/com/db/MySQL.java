package com.db;

import com.model.Image;
import com.model.User;
import com.model.Whiteboard;


public class MySQL {
    final static String CREATE_WHITEBOARDS_TABLE = "CREATE TABLE IF NOT EXISTS Whiteboards ("
        + "WhiteboardID CHAR(32) NOT NULL, "
        + "Name VARCHAR(" + Whiteboard.MAX_WB_NAME_LENGTH + "), "
        + "PRIMARY KEY(WhiteboardID))";

    final static String CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS Images ("
        + "ImageID CHAR(32) NOT NULL, "
        + "WhiteboardID CHAR(32) NOT NULL, "
        + "Filename VARCHAR(" + Image.MAX_IMG_NAME_LENGTH + "), "
        + "Bytes BLOB, "
        + "Date TIMESTAMP(6) NOT NULL, "
        + "PRIMARY KEY(ImageID))";

    // TODO: How do I do points?
    final static String CREATE_EDITS_TABLE = "CREATE TABLE IF NOT EXISTS Edits ("
        + "WhiteboardID CHAR(32) NOT NULL, "
        + "Username VARCHAR(" + User.MAX_USER_NAME_LENGTH + ") NOT NULL, "
        + "Color INT NOT NULL, "
        + "BrushSize INT NOT NULL, "
        + "Timestamp TIMESTAMP(6) NOT NULL, "
        + "UNIQUE(WhiteboardID, Username, Date))";

    final static String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users ("
        + "WhiteboardID CHAR(32) NOT NULL, "
        + "Username VARCHAR(" + User.MAX_USER_NAME_LENGTH + ") NOT NULL,"
        + "Mode TINYINT NOT NULL, "
        + "UNIQUE(WhiteboardID, Username))";

    final static String ADD_WHITEBOARD = "INSERT INTO Whiteboards (WhiteboardID, Name) VALUES (?, ?)";
    final static String REMOVE_WHITEBOARD = "DELETE FROM Whiteboards WHERE WhiteboardID = ?";
    final static String RENAME_WHITEBOARD = "UPDATE Whiteboards SET Name = ? WHERE WhiteboardID = ?";
    final static String ADD_IMAGE = "INSERT INTO Images (ImageID, WhiteboardID, Filename, Bytes, Date) VALUES (?, ?, ?, ?, ?)";
    final static String ADD_EDIT = "INSERT INTO Edits (WhiteboardID, Username, Color, BrushSize, Date) VALUES (?, ?, ?, ?, ?)";
    final static String REMOVE_EDIT = "DELETE FROM Edits WHERE WhiteboardID = ? AND Username = ? AND Timestamp = ?";
    final static String ADD_USER = "INSERT INTO Users (WhiteboardID, Username, Mode) VALUES (?, ?, ?)";
    final static String REMOVE_USER = "DELETE FROM Users WHERE WhiteboardID = ? AND Username = ?";
    final static String RENAME_USER = "UPDATE Users SET Username = ? WHERE WhiteboardID = ? AND Username = ?"; // TODO: Is this legal?
    final static String SET_USER_MODE = "UPDATE Users SET Mode = ? WHERE WhiteboardID = ? AND Username = ?";

    // TODO: Database queries
}