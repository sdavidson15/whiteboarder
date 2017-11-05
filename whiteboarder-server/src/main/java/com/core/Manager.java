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

	public static void uploadImage(Context ctx, String wbID, Image img) throws WbException {
		Logger.log.info("Uploading an image.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		} else if (wbID == null) {
			throw new WbException("SessionID cannot be empty.");
		}

		img = (img != null) ? img : new Image(wbID, "Blank Image", null);
		ctx.getDbc().getWhiteboard(wbID); // Confirm that the Whiteboard exists
		img = ctx.getDbc().addImage(img);
	}

	public static Image getImage(Context ctx, String wbID) throws WbException {
		Logger.log.info("Retrieving an image.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		ctx.getDbc().getWhiteboard(wbID); // Confirm that the Whiteboard exists
		return ctx.getDbc().getImage(wbID);
	}

	public static Edit applyEdit(Context ctx, Edit edit) throws WbException {
		Logger.log.info("Applying an edit.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		ctx.getDbc().getWhiteboard(edit.getWbID()); // Confirm that the Whiteboard exists
		// TODO: Confirm that the User exists
		return ctx.getDbc().addEdit(edit);
	}

	public static void removeEdit(Context ctx, Edit edit) throws WbException {
		Logger.log.info("Removing an edit.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		ctx.getDbc().getWhiteboard(edit.getWbID()); // Confirm that the Whiteboard exists
		// TODO: Confirm that the User exists
		ctx.getDbc().removeEdit(edit);
	}

	public static void addUser(Context ctx, String sessionID, String username) throws WbException {
		Logger.log.info("Adding a user.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		ctx.getDbc().getWhiteboard(sessionID); // Confirm that the Whiteboard exists
		User user = new User(sessionID, username, Mode.COLLABORATOR);
		ctx.getDbc().addUser(user);
	}

	public static void removeUser(Context ctx, String sessionID, String username) throws WbException {
		Logger.log.info("Removing a user.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		ctx.getDbc().getWhiteboard(sessionID); // Confirm that the Whiteboard exists
		User user = new User(sessionID, username, Mode.COLLABORATOR);
		ctx.getDbc().removeUser(user);
	}

	private static boolean isValid(Context ctx) {
		return ctx != null && ctx.isValid();
	}
}
