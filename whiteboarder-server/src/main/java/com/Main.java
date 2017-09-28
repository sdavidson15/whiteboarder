package com;

import java.io.IOException;
import org.glassfish.grizzly.http.server.HttpServer;

public class Main {
    public static void main(String[] args) throws IOException {
        


        final HttpServer server = Rest.startServer();
        System.in.read();
        server.stop();
    }
}