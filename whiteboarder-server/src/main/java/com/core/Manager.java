package com.core;

import com.db.DatabaseConnector;
import com.model.*;

// TODO: Rename this class
public class Manager {

	public static Whiteboard createSession(Context ctx) {
		if (ctx == null || ctx.user() == null || ctx.dbc() == null) {
			// TODO: Throw error "Invalid context" and print ctx
		}

		Whiteboard wb = new Whiteboard("New Whiteboard");

		ctx.dbc().addWhiteboard(wb);
		ctx.dbc().addUser(new User(wb.getWbID(), ctx.user().getUsername(), Mode.HOST));
		return wb;
	}

	public static Whiteboard uploadImg(Context ctx, String wbID, Image img) {
		if (ctx == null || ctx.user() == null || ctx.dbc() == null) {
			// TODO: Throw error "Invalid context" and print ctx
		}

		img = (img != null) ? img : new Image(wbID, "Blank Image", null);
		wbID = (wbID != null) ? wbID : "";
		Whiteboard wb = ctx.dbc().getWhiteboard(wbID);
		if (wb == null) {
			// TODO: Throw error "No such Whiteboard exists"
		}
		img = ctx.dbc().addImage(img);
		wb.addImage(img);
		return wb;
	}

}
