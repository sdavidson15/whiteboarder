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
    @Produces(APPLICATION_JSON)
    public Response createSession() {
        // TODO: Generate uuid
        // Persist data model with uuid
        // Return uuid in the response
        return null;
    }

    @POST
    @Path("/image")
    @Produces(APPLICATION_JSON)
    public Response uploadImage(String payload) {
        // TODO: Process the payload
        // TODO: Generate session id
        // TODO: Create the appropriate data model and persist
        // TODO: Return session id
        return null;
    }

    @GET
    @Path("/image")
    @Produces(APPLICATION_JSON)
    public Response getImage(String payload) {
        // FIXME: Session id comes in as a query parameter, figure out Jersey does that.
        // TODO: Validate session id
        // TODO: Get image from storage
        // TODO: Return the image in it's form
        return null;
    }
}