package com.core;

import com.db.DatabaseConnector;
import com.model.*;

public class SessionCreate {

    public static String createSessionsID() {
        // TODO - generate uuid
        return "";
    }

    public static void createSession(DatabaseConnector dbc, int userID, Image img) {
        Whiteboard wb = new Whiteboard("New Whiteboard");
        wb.addImage(img);

        // TODO: What do I do with this user?

        dbc.AddWhiteboarderSession(wb);
    }
}