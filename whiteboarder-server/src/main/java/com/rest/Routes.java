package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

import com.core.Context;
import com.core.Manager;
import com.core.Util;
import com.core.WbException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.model.*;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/whiteboarder")
public class Routes {

	public static Context ctx;

	@POST
	@Path("/session")
	@Consumes(APPLICATION_JSON)
	@Produces(TEXT_PLAIN)
	public Response createSession(String payload) {
		User user = new User("", "", Mode.HOST);
		Context userCtx = new Context(user, ctx.getDbc(), ctx.isLocal());

		Whiteboard wb;
		try {
			wb = Manager.createSession(userCtx);
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok(wb.getWbID(), TEXT_PLAIN).build();
	}

	@GET
	@Path("/image/{sessionID}")
	@Produces(APPLICATION_OCTET_STREAM)
	public Response getImage(@PathParam("sessionID") String sessionID) {
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.serverError().entity("Session ID cannot be empty.").build();
		}

		Image img;
		try {
			img = Manager.getImage(ctx, sessionID);
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok(img.getBytes(), APPLICATION_OCTET_STREAM).build();
	}

	@POST
	@Path("/image/{sessionID}")
	@Consumes(MULTIPART_FORM_DATA)
	public Response uploadImage(@FormDataParam("file") File file,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("sessionID") String sessionID) {

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
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok("Image uploaded.").build();
	}
}
