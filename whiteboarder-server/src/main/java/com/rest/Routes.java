package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

	@POST
	@Path("/session")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response createSession(String payload) {
		// TODO: Process the payload, generate session id, persist appropriate data model
		String output = "This should be JSON";
		return Response.ok(output, APPLICATION_JSON).build();
	}

	@GET
	@Path("/image/{sessionID}")
	@Produces(APPLICATION_JSON)
	public Response getImage(@PathParam("sessionID") String sessionID) {
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.serverError().entity("Session ID cannot be empty.").build();
		}
		// TODO: Return image from storage
		String output = "This should be JSON";
		return Response.ok(output, APPLICATION_JSON).build();
	}

	@POST
	@Path("/image/{sessionID}")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response uploadImage(String payload, @PathParam("sessionID") String sessionID) {
		if (sessionID == null || sessionID.trim().length() == 0) {
			return Response.serverError().entity("Session ID cannot be empty.").build();
		}
		// TODO: Process the payload, persist the appropriate data model
		String output = "This should be JSON";
		return Response.ok(output, APPLICATION_JSON).build();
	}
}
