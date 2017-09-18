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
        // TODO: Generate uuid
        // Persist data model with uuid
        // Return uuid in the response
        String output = "This should be JSON";
        return Response.ok(output, APPLICATION_JSON).build();
    }

    @POST
    @Path("/image/{sessionID}") // We can use the session ID as a query parameter, but call this behind the scenes without navigating to it.
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response uploadImage(String payload, @PathParam("sessionID") String sessionID) { // TODO: Make sure this works, with both the payload and the sessionID
        if (sessionID == null || sessionID.trim().length() == 0) {
            return Response.serverError().entity("Session ID cannot be empty.").build();
        }
        // TODO: Process the payload
        // TODO: Generate session id - only for milestone 1. Otherwise, the session ID is passed in.
        // TODO: Create the appropriate data model and persist
        // TODO: Return session id
        String output = "This should be JSON";
        return Response.ok(output, APPLICATION_JSON).build();
    }

    @GET
    @Path("/image/{sessionID}") // We can use the session ID as a query parameter, but call this behind the scenes without navigating to it.
    @Produces(APPLICATION_JSON)
    public Response getImage(@PathParam("sessionID") String sessionID) {
        if (sessionID == null || sessionID.trim().length() == 0) {
            return Response.serverError().entity("Session ID cannot be empty.").build();
        }
        // TODO: Validate session id
        // TODO: Get image from storage
        // TODO: Return the image in it's form
        String output = "This should be JSON";
        return Response.ok(output, APPLICATION_JSON).build();
    }
}