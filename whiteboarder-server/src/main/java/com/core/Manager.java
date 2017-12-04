package com.core;

import com.db.DatabaseConnector;
import com.model.*;

import java.util.List;
import java.util.Set;

public class Manager {

	public static Whiteboard getSession(Context ctx, String wbID) throws WbException {
		Logger.log.info("Retrieving a session.");
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);

		Whiteboard wb = ctx.getDbc().getWhiteboard(wbID);
		List<Edit> edits = ctx.getDbc().getEdits(wbID);
		for (Edit edit : edits)
			edit.setPoints(ctx.getDbc().getPoints(edit.getEditID()));
		wb.setEdits(edits);
		return wb;
	}

	public static Whiteboard createSession(Context ctx) throws WbException {
		Logger.log.info("Creating a session.");
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);
		else if (ctx.getUser() == null)
			throw new WbException(WbException.INVALID_USER);

		Whiteboard wb = new Whiteboard("New Whiteboard");

		ctx.getDbc().addWhiteboard(wb);
		return wb;
	}

	public static void renameSession(Context ctx, String wbID, String newName) throws WbException {
		Logger.log.info("Renaming a session.");
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);
		else if (wbID == null)
			throw new WbException(WbException.INVALID_SESSION);
		else if (newName == null)
			throw new WbException(WbException.INVALID_NAME);

		ctx.getDbc().renameWhiteboard(wbID, newName);
	}

	public static void uploadImage(Context ctx, String wbID, Image img) throws WbException {
		Logger.log.info("Uploading an image.");
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);
		else if (wbID == null)
			throw new WbException(WbException.INVALID_SESSION);

		img = (img != null) ? img : new Image(wbID, "Blank Image", null);
		ctx.getDbc().getWhiteboard(wbID); // Confirm that the Whiteboard exists
		img = ctx.getDbc().addImage(img);
	}

	public static Image getImage(Context ctx, String wbID) throws WbException {
		Logger.log.info("Retrieving an image.");
		if (!isValid(ctx))
			throw new WbException(WbException.INVALID_CONTEXT);

		ctx.getDbc().getWhiteboard(wbID); // Confirm that the Whiteboard exists
		return ctx.getDbc().getImage(wbID);
	}

	public static Edit applyEdit(Context ctx, Edit edit) throws WbException {
		Logger.log.info("Applying an edit.");
		if (!isValid(ctx)) {
			throw new WbException(WbException.INVALID_CONTEXT);
		}

		ctx.getDbc().getWhiteboard(edit.getWbID()); // Confirm that the Whiteboard exists
		ctx.getDbc().getUser(edit.getWbID(), edit.getUsername()); // Confirm that the User exists
		Edit storedEdit = ctx.getDbc().addEdit(edit);
		for (Point p : edit.getPoints())
			p.setEditID(storedEdit.getEditID());
		ctx.getDbc().addPoints(edit.getPoints());
		return storedEdit;
	}

	public static void removeEdit(Context ctx, Edit edit) throws WbException {
		Logger.log.info("Removing an edit.");
		if (!isValid(ctx)) {
			throw new WbException(WbException.INVALID_CONTEXT);
		}

		ctx.getDbc().getWhiteboard(edit.getWbID()); // Confirm that the Whiteboard exists
		ctx.getDbc().getUser(edit.getWbID(), edit.getUsername()); // Confirm that the User exists
		ctx.getDbc().removeEdit(edit);
		ctx.getDbc().removePoints(edit.getEditID());
	}

	public static void addUser(Context ctx, String sessionID, String username) throws WbException {
		Logger.log.info("Adding a user.");
		if (!isValid(ctx)) {
			throw new WbException(WbException.INVALID_CONTEXT);
		}

		ctx.getDbc().getWhiteboard(sessionID); // Confirm that the Whiteboard exists
		User user = new User(sessionID, username, Mode.COLLABORATOR);
		ctx.getDbc().addUser(user);
	}

	public static void removeUser(Context ctx, String sessionID, String username) throws WbException {
		Logger.log.info("Removing a user.");
		if (!isValid(ctx)) {
			throw new WbException(WbException.INVALID_CONTEXT);
		}

		ctx.getDbc().getWhiteboard(sessionID); // Confirm that the Whiteboard exists
		User user = new User(sessionID, username, Mode.COLLABORATOR);
		ctx.getDbc().removeUser(user);
	}

	public static Message sendMessage(Context ctx, Message msg) throws WbException {
		Logger.log.info("Sending a message.");
		if (!isValid(ctx)) {
			throw new WbException(WbException.INVALID_CONTEXT);
		}

		ctx.getDbc().getWhiteboard(msg.getWbID());
		ctx.getDbc().getUser(msg.getWbID(), msg.getUsername());
		Message storedMsg = ctx.getDbc().addMessage(msg);
		return storedMsg;
	}

	public static void removeMessage(Context ctx, Message msg) throws WbException {
		Logger.log.info("Deleting a message.");
		if (!isValid(ctx)) {
			throw new WbException(WbException.INVALID_CONTEXT);
		}

		ctx.getDbc().getWhiteboard(msg.getWbID());
		ctx.getDbc().getUser(msg.getWbID(), msg.getUsername());
		ctx.getDbc().removeMessage(msg);
	}

	public static Set<User> getUsers(Context ctx, String sessionID) throws WbException {
		Logger.log.info("Retrieving users.");
		if (!isValid(ctx)) {
			throw new WbException("Invalid context");
		}

		ctx.getDbc().getWhiteboard(sessionID); // Confirm that the Whiteboard exists
		return ctx.getDbc().getUsers(sessionID);
	}

	private static boolean isValid(Context ctx) {
		return ctx != null && ctx.isValid();
	}
}
