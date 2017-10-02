package com.db;

import com.model.*;
import java.sql.*;
import javax.sql.rowset.serial.SerialBlob;

public class DatabaseConnector {

    private Connection c;

    public DatabaseConnector(String host, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.c = DriverManager.getConnection(host, username, password);
        } catch (SQLException e) {
            // TODO
            return;
        } catch (Exception e) {
            // TODO
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
            // TODO
        }
    }

    public void endConnection() {
        try {
            c.close();
        } catch (SQLException e) {
            // TODO
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
            // TODO
        }
    }

    public void removeWhiteboarderSession(Whiteboard wb) {
        try {
            PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_WHITEBOARD);
            stmt.setString(1, wb.getWbID());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO
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
            // TODO
        }
    }

    public void addImage(Image img) {
        try {
            PreparedStatement stmt = c.prepareStatement(MySQL.ADD_IMAGE);
            stmt.setString(1, img.getImgID());
            stmt.setString(2, img.getWbID());
            stmt.setString(3, img.getFilename());
            stmt.setBlob(4, (Blob) new SerialBlob(img.getBytes()));
            stmt.setTimestamp(5, new Timestamp(img.getTimestamp().getTime()));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO
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
            // TODO
        }
    }

    public void removeEdit(Edit edit) {
        try {
            PreparedStatement stmt = c.prepareStatement(MySQL.REMOVE_EDIT);
            stmt.setString(1, edit.getWbID());
            stmt.setString(2, edit.getUsername());
            stmt.setTimestamp(3, new Timestamp(edit.getTimestamp().getTime()));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO
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
            // TODO
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
            // TODO
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
            // TODO
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
            // TODO
            return null;
        }

        return new Whiteboard(wbID, name);
    }
}