package com.core;

public class WbException extends Exception {
    public WbException(String msg) {
        super(msg);
    }

    public WbException(String msg, Exception e) {
        super(msg, e);
    }

    public static final String DB_START_CONNECTION = "Failed to connect to the MySQL database.";
    public static final String DB_CLEAR_TABLES = "Failed to clear database tables.";
    public static final String DB_INIT_TABLES = "Failed to initialize database tables.";
    public static final String DB_END_CONNECTION = "Failed to end connection to the MySQL database.";
    public static final String DB_ADD_WB = "Failed to insert a Whiteboard into the database.";
    public static final String DB_REMOVE_WB = "Failed to remove a Whiteboard from the database.";
    public static final String DB_RENAME_WB = "Failed to rename a Whiteboard in the database.";
    public static final String DB_ADD_IMG = "Failed to insert an Image into the database.";
    public static final String DB_ADD_EDIT = "Failed to insert an Edit into the database.";
    public static final String DB_ADD_BATCHED_EDITS = "Failed to insert a batch of Edits into the database.";
    public static final String DB_REMOVE_EDIT = "Failed to remove an Edit from the database.";
    public static final String DB_REMOVE_BATCHED_EDITS = "Failed to remove a batch of Edits from the database.";
    public static final String DB_ADD_USER = "Failed to insert a User into the database.";
    public static final String DB_RENAME_USER = "Failed to rename a User in the database.";
    public static final String DB_SET_USER_MODE = "Failed to change a User's mode in the database.";
    public static final String DB_GET_WB = "Failed to retrieve a Whiteboard from the database.";
    public static final String DB_GET_IMG = "Failed to retrieve an Image from the database.";
    public static final String DB_GET_IMGS = "Failed to retrieve Images from the database.";
    public static final String DB_GET_EDITS = "Failed to retrieve Edits from the database.";
    public static final String DB_INVALID_MODE = "Mode creation was rejected";
    public static final String DB_GET_USER = "Failed to retrieve User from the database.";
    public static final String DB_GET_USERS = "Failed to retrieve Users from the database.";
    public static final String DB_REMOVE_USER = "Failed to remove a User from the database.";
    public static final String DB_ADD_POINTS = "Failed to insert a set of Points into the database.";
    public static final String DB_REMOVE_POINTS = "Failed to remove a set of Points from the database.";
    public static final String DB_GET_POINTS = "Failed to retrieve Points from the database.";
    public static final String DB_ADD_MESSAGE = "Failed to send Message.";
    public static final String DB_REMOVE_MESSAGE = "Failed to remove Message.";

    public static final String INVALID_USER = "Failed to create session with a null user.";
    public static final String INVALID_CONTEXT = "Invalid context.";
    public static final String INVALID_NAME = "Failed to create session with a null name.";
    public static final String INVALID_SESSION = "SessionID cannot be empty.";

    public static final String WHITEBOARD_DNE = "Whiteboard does not exist.";
    public static final String USER_DNE = "User does not exist.";
}