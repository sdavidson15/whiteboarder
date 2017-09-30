package com.db;

import com.model.*;
import java.sql.*;

public class DatabaseConnector {

    // TODO: Error handling

    private Connection c;

    public DatabaseConnector(String host, String username, String password) {
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver"); // TODO: Figure out wtf this does
            connection = DriverManager.getConnection(host, username, password);
        } catch (SQLException e) {
            // TODO
        }
        this.c = connection;
        initTables();
    }

    private void initTables() {
        Statement stmt = c.createStatement();
        stmt.addBatch(MySQL.CREATE_WHITEBOARDS_TABLE);
        stmt.addBatch(MySQL.CREATE_IMAGES_TABLE);
        stmt.addBatch(MySQL.CREATE_EDITS_TABLE);
        stmt.addBatch(MySQL.CREATE_USERS_TABLE);
        stmt.executeBatch();
        stmt.close();
    }

    public void endConnection() {
        c.close();
    }

    public void addWhiteboarderSession(Whiteboard wb) {
        PreparedStatement stmt = c.prepareStatement(MySQL.ADD_WHITEBOARD);
        stmt.setString(1, wb.getWbID());
        stmt.setString(2, wb.getName());
        stmt.executeUpdate();
        stmt.close();
    }

    public void removeWhiteboarderSession(Whiteboard wb) {
        PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_WHITEBOARD);
        stmt.setString(1, wb.getWbID());
        stmt.executeUpdate();
        stmt.close();
    }

    public void renameWhiteboarderSession(String wbID, String newName) {
        PreparedStatement stmt = c.prepareStatement(MySQL.RENAME_WHITEBOARD);
        stmt.setString(1, newName);
        stmt.setString(2, wbID);
        stmt.executeUpdate();
        stmt.close();
    }

    public void addImage(Image img) {
        PreparedStatement stmt = c.prepareStatement(MySQL.ADD_IMAGE);
        stmt.setString(1, img.getImgID());
        stmt.setString(2, img.getWbID());
        stmt.setString(3, img.getFilename());
        stmt.setBlob(4, img.getBytes()); // TODO: Make sure byte[] is an InputStream
        stmt.setTimestamp(5, new Timestamp(img.getTimestamp().getTime()));
        stmt.executeUpdate();
        stmt.close();
    }

    public void addEdit(Edit edit) {
        PreparedStatement stmt = c.prepareStatement(MySQL.ADD_EDIT);
        stmt.setString(1, edit.getWbID());
        stmt.setString(2, edit.getUsername());
        stmt.setInt(3, edit.getColor());
        stmt.setInt(4, edit.getBrushSize());
        stmt.setTimestamp(5, new Timestamp(edit.getTimestamp().getTime()));
        stmt.executeUpdate();
        stmt.close();
    }

    public void removeEdit(Edit edit) {
        PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_EDIT);
        stmt.setString(1, edit.getWbID());
        stmt.setString(2, edit.getUsername());
        stmt.setTimestamp(3, new Timestamp(edit.getTimestamp().getTime()));
        stmt.executeUpdate();
        stmt.close();
    }

    public void addUser(User user) {
        PreparedStatement stmt = c.prepareStatement(MySQL.ADD_USER);
        stmt.setString(1, user.getWbID());
        stmt.setString(2, user.getUsername());
        stmt.setInt(3, user.getMode().getValue());
        stmt.executeUpdate();
        stmt.close();
    }

    public void renameUser(User user, String newName) {
        PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_USER);
        stmt.setString(1, user.getWbID());
        stmt.setString(2, user.getUsername());
        stmt.executeUpdate();
        stmt.close();
    }

    public void setUserMode(User user, Mode mode) {
        PreparedStatement stmt = c.prepareStatement(MySQL.SET_USER_MODE);
        stmt.setInt(1, user.getMode().getValue());
        stmt.setString(2, user.getWbID());
        stmt.setString(3, user.getUsername());
        stmt.executeUpdate();
        stmt.close();
    }

    // TODO: Database queries
}