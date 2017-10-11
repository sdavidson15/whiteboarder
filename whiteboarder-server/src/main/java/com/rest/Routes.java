package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import com.core.Context;
import com.core.Manager;
import com.core.WbException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.model.*;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import jdk.nashorn.internal.objects.annotations.Getter;

@Path("/whiteboarder")
public class Routes {

	public static Context ctx;

	@POST
	@Path("/session")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response createSession(String payload) {
		// TODO: Collect user information from this request to form ctx
		// Username from the payload?
		User user = null;
		Context userCtx = new Context(user, ctx.dbc(), ctx.isLocal());

		Whiteboard wb;
		try {
			wb = Manager.createSession(userCtx);
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok(wb.getWbID(), APPLICATION_JSON).build();
	}

	@GET
	@Path("/image/{sessionID}")
	@Produces(APPLICATION_JSON)
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

		Gson gson = new GsonBuilder().create();
		String resp = img != null ? gson.toJson(img) : null;
		return Response.ok(resp, APPLICATION_JSON).build();
	}

	@POST
	@Path("/image/{sessionID}")
	@Consumes(APPLICATION_JSON)
	public Response uploadImage(@PathParam("sessionID") String sessionID, String payload) {
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.status(400).entity("Session ID cannot be empty.").build();
		}

		Image img;
		try {
			Gson gson = new GsonBuilder().create();
			img = gson.fromJson(payload, new TypeToken<Image>() {
			}.getType());
			img.setTimestamp(new Date());
			img.setImgID(-1);
		} catch (JsonSyntaxException e) {
			return Response.status(400).entity("Incorrect JSON format for the image payload.").build();
		} catch (Exception e) {
			return Response.serverError().entity(e.toString()).build();
		}

		try {
			Manager.uploadImage(ctx, sessionID, img);
		} catch (WbException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}
}
