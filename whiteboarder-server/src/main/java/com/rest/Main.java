package com.rest;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/whiteboarder/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.rest");
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        // serve static files on `/`
        final HttpHandler httpHandler = new StaticHttpHandler("../whiteboarder-web");
        server.getServerConfiguration().addHttpHandler(httpHandler, "/");

        return server;
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.in.read();
        server.shutdownNow();
    }
}

