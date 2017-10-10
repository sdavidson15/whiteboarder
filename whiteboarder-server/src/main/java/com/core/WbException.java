package com.core;

public class WbException extends Exception {
    public static final String DB_CONNECTION = "Failed to connect to the MySQL database.";
    public static final String DB_CLEAR_TABLES = "Failed to clear database tables.";
    public static final String DB_INIT_TABLES = "Failed to initialize database tables.";
    public static final String DB_END_CONNECTION = "Failed to end connection to the MySQL database.";
    public static final String DB_ADD_WB = "Failed to insert a Whiteboard into the database.";
}