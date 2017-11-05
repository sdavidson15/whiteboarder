package com.core;

import com.db.DatabaseConnector;
import com.model.*;

public class Manager {

	public static Whiteboard createSession(Context ctx) throws WbException {
		Logger.log.info("Creating a session.");
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
		Logger.log.info("Uploading an image.");
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
		Logger.log.info("Retrieving an image.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		return ctx.getDbc().getImage(wbID);
	}

	public static void applyEdit(Context ctx, Edit edit) throws WbException {
		Logger.log.info("Applying an edit.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}
		// TODO:
	}

	public static void removeEdit(Context ctx, Edit edit) throws WbException {
		Logger.log.info("Removing an edit.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}
		// TODO:
	}

	public static void addUser(Context ctx, String sessionID, String username) throws WbException {
		Logger.log.info("Adding a user.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}
		// TODO:
	}

	public static void removeUser(Context ctx, String sessionID, String username) throws WbException {
		Logger.log.info("Removing a user.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}
		// TODO:
	}

	private static boolean isValid(Context ctx) {
		return ctx != null && ctx.isValid();
	}
}
