package com.core;

import com.db.DatabaseConnector;
import com.model.*;

public class Manager {

	public static Whiteboard createSession(Context ctx) throws WbException {
		if (ctx == null || ctx.user() == null || ctx.dbc() == null) {
			throw new WbException("Invalid context");
		}

		Whiteboard wb = new Whiteboard("New Whiteboard");

		ctx.dbc().addWhiteboard(wb);
		return wb;
	}

	public static Whiteboard uploadImage(Context ctx, String wbID, Image img) throws WbException {
		if (ctx == null || ctx.dbc() == null) {
			throw new WbException("Invalid context");
		}

		img = (img != null) ? img : new Image(wbID, "Blank Image", null);
		wbID = (wbID != null) ? wbID : "";
		Whiteboard wb = ctx.dbc().getWhiteboard(wbID);
		img = ctx.dbc().addImage(img);
		wb.addImage(img);
		return wb;
	}

	public static Image getImage(Context ctx, String wbID) throws WbException {
		if (ctx == null || ctx.dbc() == null) {
			throw new WbException("Invalid context");
		}

		return ctx.dbc().getImage(wbID);
	}

}
