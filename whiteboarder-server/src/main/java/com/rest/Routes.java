package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import com.core.Context;
import com.core.Manager;
import com.core.Logger;
import com.core.Util;
import com.core.WbException;
import com.model.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/")
public class Routes {

	// internal class used for renaming whiteboards or users
	// exists for JSON serialization
	class RenameRequest {
		String sessionID, newName, oldName;

		public RenameRequest(String sessionID, String newName, String oldName) {
			this.sessionID = sessionID;
			this.newName = newName;
			this.oldName = oldName;
		}
	}

	public static Context ctx;

	@GET
	@Path("/session/{sessionID}")
	@Produces(APPLICATION_JSON)
	public Response getSession(@PathParam("sessionID") String sessionID) {
		Whiteboard wb = null;
		try {
			wb = Manager.getSession(ctx, sessionID);
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		Gson gson = new GsonBuilder().create();
		return Response.ok(gson.toJson(wb), APPLICATION_JSON).build();
	}

	@POST
	@Path("/session")
	@Produces(TEXT_PLAIN)
	public Response createSession(String payload) {
		Logger.log.info("Recieved a POST session request.");
		User user = new User("", "", Mode.HOST);
		Context userCtx = new Context(user, ctx.getDbc(), ctx.isLocal());

		Whiteboard wb;
		try {
			wb = Manager.createSession(userCtx);
		} catch (WbException e) {
			Logger.log.severe("Error POSTing image: " + e.getMessage());
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok(wb.getWbID(), TEXT_PLAIN).build();
	}

	@PUT
	@Path("/session")
	@Consumes(APPLICATION_JSON)
	@Produces(TEXT_PLAIN)
	public Response renameSession(String payload) {
		RenameRequest rr;
		try {
			Gson gson = new GsonBuilder().create();
			rr = gson.fromJson(payload, new TypeToken<RenameRequest>() {
			}.getType());

			Manager.renameSession(ctx, rr.sessionID, rr.newName);

		} catch (JsonSyntaxException e) {
			return Response.status(400).entity("Incorrect JSON format").build();
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok("Session renamed.").build();
	}

	@GET
	@Path("/image/{sessionID}")
	@Produces(APPLICATION_OCTET_STREAM)
	public Response getImage(@PathParam("sessionID") String sessionID) {
		Logger.log.info("Recieved a GET image request.");
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.serverError().entity("Session ID cannot be empty.").build();
		}

		Image img;
		try {
			img = Manager.getImage(ctx, sessionID);
		} catch (WbException e) {
			Logger.log.severe("Error GETting image: " + e.getMessage());
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok(img.getBytes(), APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "inline; filename=\"" + sessionID + ".jpg\"").build();
	}

	@POST
	@Path("/image/{sessionID}")
	@Consumes(MULTIPART_FORM_DATA)
	public Response uploadImage(@FormDataParam("file") File file,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("sessionID") String sessionID) {

		Logger.log.info("Recieved a POST image request.");
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.status(400).entity("Session ID cannot be empty.").build();
		}

		try {
			Image img = null;
			if (file != null && fileDetail != null) {
				byte[] data = Util.fileToByteArr(file);
				img = new Image(sessionID, fileDetail.getFileName(), data);
			}
			Manager.uploadImage(ctx, sessionID, img);
		} catch (WbException e) {
			Logger.log.severe("Error POSTing image: " + e.getMessage());
			return Response.serverError().entity(e.getMessage()).build();
		}

		// Inform all clients to refresh the background image
		ctx.getWbApp().refreshImage(sessionID);

		return Response.ok("Image uploaded.").build();
	}

	@GET
	@Path("/users/{sessionID}")
	public Response getUsers(@PathParam("sessionID") String sessionID) {
		Logger.log.info("Recieved a GET users request.");
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.status(400).entity("Session ID cannot be empty.").build();
		}

		Set<User> users = null;
		try {
			users = Manager.getUsers(ctx, sessionID);
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		Gson gson = new GsonBuilder().create();
		return Response.ok(gson.toJson(users), APPLICATION_JSON).build();
	}
}
