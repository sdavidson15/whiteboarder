package com.rest;

import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Rest {
	public static final String BASE_URI = "http://proj-309-yt-c-1.cs.iastate.edu/";
	public static final String LOCAL_BASE_URI = "http://localhost:8080/whiteboarder/";

	public static HttpServer startServer(boolean isLocal) {
		final ResourceConfig rc = new ResourceConfig().packages("com.rest");
		String uri = isLocal ? LOCAL_BASE_URI : BASE_URI;
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
	}
}