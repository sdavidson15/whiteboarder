package com.rest;

import com.core.Context;

import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Rest {
	public static final String BASE_URI = "http://proj-309-yt-c-1.cs.iastate.edu/whiteboarder/";
	public static final String LOCAL_BASE_URI = "http://localhost/whiteboarder/";

	// TODO: This no longer starts the server. Might need to rename this.
	public static HttpServer startServer(Context ctx) {
		Routes.ctx = ctx;

		ResourceConfig rc = new ResourceConfig().packages("com.rest").register(MultiPartFeature.class);

		String uriStr = ctx.isLocal() ? LOCAL_BASE_URI : BASE_URI;
		final URI uri = UriBuilder.fromUri(uriStr).port(8080).build();

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);

		// serve static files on "/"
		HttpHandler httpHandler = new StaticHttpHandler("../whiteboarder-web");
		server.getServerConfiguration().addHttpHandler(httpHandler, "/");

		return server;
	}
}
