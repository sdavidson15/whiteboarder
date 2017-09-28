package com.db;

import com.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private Connection c;

    public DatabaseConnector(String host, String username, String password) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(host, username, password);
        } catch (SQLException e) {
            // TODO: Handle SQL error
        }
        this.c = connection;
    }

    public void AddWhiteboarderSession(Whiteboard wb) {
        // TODO
    }

    public void RenameWhiteboarderSession(String wbID, String name) {
        // TODO
    }

    public void AddEdit(String wbID, Edit edit) {
        // TODO
    }

    public void AddImage(String wbID, Image img) {
        // TODO
    }

    public void AddUser(String wbID, User user) {
        // TODO
    }

    public void RenameUser(String wbID, int userID, String name) {
        // TODO
    }
}