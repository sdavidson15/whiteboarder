package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.db.DatabaseConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.core.SessionCreate;
import com.model.Image;
import com.model.Whiteboard;
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

@Path("/wb")
public class Routes {

	public static DatabaseConnector dbc;

	@POST
	@Path("/session")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response createSession(String payload) {
		Gson gson = new GsonBuilder().create();

		Image img = new Image(null, "Blank Image", null);
		if (payload != null && payload.length() > 0) {
			try {
				img = gson.fromJson(payload, new TypeToken<Image>() {}.getType());
			} catch(JsonSyntaxException e) {
				return Response.status(400).entity("Incorrect JSON format for the image payload.").build();
			} catch(Exception e) {
				return Response.serverError().entity(e.toString()).build();
			}
		}

		Whiteboard wb = SessionCreate.createSession(null, img);
		return Response.ok(wb.getWbID(), APPLICATION_JSON).build();
	}

	@GET
	@Path("/image/{sessionID}")
	@Produces(APPLICATION_JSON)
	public Response getImage(@PathParam("sessionID") String sessionID) {
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.serverError().entity("Session ID cannot be empty.").build();
		}
		Gson gson = new GsonBuilder().create();

		Image img = dbc.getImage(sessionID);
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
		Gson gson = new GsonBuilder().create();

		Image img = new Image(null, "Blank Image", null);
		try {
			img = gson.fromJson(payload, new TypeToken<Image>() {}.getType());
			img.setTimestamp(new Date());
		} catch(JsonSyntaxException e) {
			return Response.status(400).entity("Incorrect JSON format for the image payload.").build();
		} catch(Exception e) {
			return Response.serverError().entity(e.toString()).build();
		}

		Whiteboard wb = new Whiteboard(sessionID, "wbName");
		img.setWbID(sessionID);
		wb.addImage(img);

		dbc.addWhiteboarderSession(wb);
		dbc.addImage(img);
		
		return Response.ok().build();
	}
}
