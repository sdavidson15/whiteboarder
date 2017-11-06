package com.core;

import com.db.DatabaseConnector;
import com.model.*;

public class Manager {

	public static Whiteboard getSession(Context ctx, String wbID) throws WbException {
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);

		Whiteboard wb = ctx.getDbc().getWhiteboard(wbID);
		wb.setEdits(ctx.getDbc().getEdits(wbID));
		return wb;
	}

	public static Whiteboard createSession(Context ctx) throws WbException {
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);
		else if (ctx.getUser() == null)
			throw new WbException(WbException.INVALID_USER);

		Whiteboard wb = new Whiteboard("New Whiteboard");

		ctx.getDbc().addWhiteboard(wb);
		return wb;
	}

	public static void renameSession(Context ctx, String wbID, String newName) throws WbException {
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);
		else if (wbID == null)
			throw new WbException(WbException.INVALID_SESSION);
		else if (newName == null)
			throw new WbException(WbException.INVALID_NAME);

		ctx.getDbc().renameWhiteboard(wbID, newName);
	}

	public static Whiteboard uploadImage(Context ctx, String wbID, Image img) throws WbException {
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);
		else if (wbID == null)
			throw new WbException(WbException.INVALID_SESSION);

		img = (img != null) ? img : new Image(wbID, "Blank Image", null);
		Whiteboard wb = ctx.getDbc().getWhiteboard(wbID);
		img = ctx.getDbc().addImage(img);
		wb.addImage(img);
		return wb;
	}

	public static Image getImage(Context ctx, String wbID) throws WbException {
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);

		return ctx.getDbc().getImage(wbID);
	}

	private static boolean isValid(Context ctx) {
		return ctx != null && ctx.isValid();
	}

}
