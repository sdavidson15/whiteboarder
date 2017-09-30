package com.db;

import com.model.Image;
import com.model.User;
import com.model.Whiteboard;


public class MySQL {

    // TODO: Create a set of all the sql commands you expect.

    final String CREATE_WHITEBOARDS_TABLE = "CREATE TABLE IF NOT EXISTS Whiteboards ("
        + "WhiteboardID char(32) NOT NULL, "
        + "Name varchar(" + Whiteboard.MAX_WB_NAME_LENGTH + "), "
        + "PRIMARY KEY(WhiteboardID))";

    // TODO: Order by WhiteboardID, then by timestamp (is that possible?)
    // TODO: You will only add to this table
    final String CREATE_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS Images ("
        + "ImageID char(32) NOT NULL, "
        + "WhiteboardID char(32) NOT NULL, "
        + "Filename varchar(" + Image.MAX_IMG_NAME_LENGTH + "), "
        + "Bytes BLOB, "
        + "Date TIMESTAMP(6) NOT NULL, "
        + "PRIMARY KEY(ImageID))"; // TODO: Maybe not?

    // TODO: Order by WhiteboardID, then by timestamp (is that possible?)
    // TODO: You will add and delete from this table
    final String CREATE_EDITS_TABLE = "CREATE TABLE IF NOT EXISTS Edits ("
        + "UserID char(32) NOT NULL, "
        + "WhiteboardID char(32) NOT NULL, "
        + "Color INT NOT NULL, "
        + "BrushSize INT NOT NULL, "
        + "Date TIMESTAMP(6) NOT NULL, "
        + "UNIQUE(UserID, Date))";

    final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users ("
        + "UserID char(32) NOT NULL, "
        + "WhiteboardID char(32) NOT NULL, "
        + "Username varchar(" + User.MAX_USER_NAME_LENGTH + "), NOT NULL"
        + "Mode TINYINT NOT NULL, "
        + "PRIMARY KEY(UserID))";

    final String ADD_WHITEBOARD = "INSERT"; // TODO
    final String REMOVE_WHITEBOARD = "DELETE FROM Whiteboards WHERE"; // TODO
    final String RENAME_WHITEBOARD = "UPDATE Whiteboards SET Name = ?"; // TODO
    final String ADD_IMAGE = "INSERT"; // TODO
    final String ADD_EDIT = "INSERT"; // TODO
    final String REMOVE_EDIT = "DELETE FROM Edits WHERE"; // TODO
    final String ADD_USER = "INSERT"; // TODO
    final String REMOVE_USER = "DELETE FROM Users WHERE"; // TODO
    final String RENAME_USER = "UPDATE Users SET Name = ?"; // TODO
}