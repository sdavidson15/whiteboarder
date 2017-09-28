package com.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.model.Image;
import com.model.Whiteboard;
import java.lang.reflect.Type;
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

    @POST
    @Path("/session")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createSession(String payload) {
        Gson gson = new GsonBuilder().create();

        Image img = new Image("Blank Image", null);
        if (payload != null && payload.length() > 0) {
            try {
                // TODO: If this fails, will img still be reassigned?
                img = gson.fromJson(payload, new TypeToken<Image>() {}.getType());
            } catch(JsonSyntaxException e) {
                return Response.status(400).entity("Incorrect JSON format for the image payload.").build();
            } catch(Exception e) {
                return Response.serverError().entity(e.toString()).build();
            }
        }

        Whiteboard wb = new Whiteboard("New Whiteboard");
        wb.addImage(img);

        // TODO: Store the image, even if bytes is null/empty.
        // TODO: Store the whiteboard
        
        return Response.ok(wb.getUUID(), APPLICATION_JSON).build();
    }

    @GET
    @Path("/image/{sessionID}")
    @Produces(APPLICATION_JSON)
    public Response getImage(@PathParam("sessionID") String sessionID) {
        if (sessionID == null || sessionID.trim().length() == 0) {
            return Response.serverError().entity("Session ID cannot be empty.").build();
        }
        Gson gson = new GsonBuilder().create();

        Whiteboard wb = new Whiteboard(sessionID); // FIXME: Get Whiteboard from storage by session ID
        if (wb == null) {
            return Response.status(100).entity("Invalid session ID.").build();
        }

        Image img = wb.getCurrentImage();
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

        Image img = new Image("Blank Image", null);
        try {
            // TODO: If this fails, will img still be reassigned?
            img = gson.fromJson(payload, new TypeToken<Image>() {}.getType());
        } catch(JsonSyntaxException e) {
            return Response.status(400).entity("Incorrect JSON format for the image payload.").build();
        } catch(Exception e) {
            return Response.serverError().entity(e.toString()).build();
        }

        Whiteboard wb = new Whiteboard(sessionID); // FIXME: Get Whiteboard from storage by session ID
        if (wb == null) {
            return Response.status(100).entity("Invalid session ID.").build();
        }

        wb.addImage(img);

        // TODO: Store the image, even if bytes is null/empty.
        // TODO: Store the whiteboard, with the new image in it's image history

        return Response.ok().build();
    }
}