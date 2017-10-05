package com.core;

import com.db.DatabaseConnector;
import com.model.*;

public class SessionCreate {

	public static String createSessionsID() {
		// TODO - generate uuid
		return "";
	}
	
	public static Whiteboard createSession(DatabaseConnector dbc, Image img) {
		Whiteboard wb = new Whiteboard("New Whiteboard");

		// For now, this is all sorts of messed up. The Whiteboard should be created before
		// the picture is taken.
		// But for demo 2, we'll allow the creation of a Whiteboard to occur with an uploaded
		// image.
		img.setWbID(wb.getWbID());
		wb.addImage(img);

		User user = new User(wb.getWbID(), "Host", Mode.HOST);

		dbc.addWhiteboarderSession(wb);
		dbc.addImage(img);
		dbc.addUser(user);

		return wb;
	}

}
