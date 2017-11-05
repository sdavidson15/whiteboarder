package com.core;

import com.db.DatabaseConnector;
import com.model.*;

public class Manager {

	public static Whiteboard createSession(Context ctx) throws WbException {
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		} else if (ctx.getUser() == null) {
			throw new WbException("Failed to create session with a null user.");
		}

		Whiteboard wb = new Whiteboard("New Whiteboard");

		ctx.getDbc().addWhiteboard(wb);
		return wb;
	}

	public static Whiteboard uploadImage(Context ctx, String wbID, Image img) throws WbException {
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		} else if (wbID == null) {
			throw new WbException("SessionID cannot be empty.");
		}

		img = (img != null) ? img : new Image(wbID, "Blank Image", null);
		Whiteboard wb = ctx.getDbc().getWhiteboard(wbID);
		img = ctx.getDbc().addImage(img);
		wb.addImage(img);
		return wb;
	}

	public static Image getImage(Context ctx, String wbID) throws WbException {
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		return ctx.getDbc().getImage(wbID);
	}

	private static boolean isValid(Context ctx) {
		return ctx != null && ctx.isValid();
	}

	public static void applyEdit(Context ctx, Edit edit) {
		Logger.log.info("Manager.applyEdit");
		// TODO:
	}

	public static void removeEdit(Context ctx, Edit edit) {
		Logger.log.info("Manager.removeEdit");
		// TODO:
	}

	public static void addUser(Context ctx, String sessionID, String username) {
		Logger.log.info("Manager.addUser");
		// TODO:
	}

	public static void removeUser(Context ctx, String sessionID, String username) {
		Logger.log.info("Manager.removeUser");
		// TODO:
	}

}
