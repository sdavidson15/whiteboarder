package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/wb")
public class HelloWorld {
    @GET
    @Path("/hello-world")
    @Produces(APPLICATION_JSON)
    public Response getHelloWorld() {
        String output = "Hello, World!";
        return Response.ok(output, APPLICATION_JSON).build();
    }

    @POST
    @Path("/hello-world")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response postHelloWorld(String data) {
        String output = "Data received: "+data;
        return Response.ok(output, APPLICATION_JSON).build();
    }

    @GET
    @Path("/{text}")
    @Produces(APPLICATION_JSON)
    public Response getText(@PathParam("text") String text) {
        if (text == null || text.trim().length() == 0) {
            return Response.serverError().entity("Text cannot be empty.").build();
        }
        String output = "Text recieved: "+text;
        return Response.ok(output, APPLICATION_JSON).build();
    }
}
