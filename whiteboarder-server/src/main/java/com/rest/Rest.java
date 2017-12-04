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
	public static HttpServer setupServer(Context ctx, String uriStr, int port) {
		Routes.ctx = ctx;

		ResourceConfig rc = new ResourceConfig().packages("com.rest").register(MultiPartFeature.class);

		final URI uri = UriBuilder.fromUri(uriStr).port(port).build();
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);

		// serve static files on "/"
		HttpHandler httpHandler = new StaticHttpHandler("../whiteboarder-web");
		server.getServerConfiguration().addHttpHandler(httpHandler, "/");

		return server;
	}
}
