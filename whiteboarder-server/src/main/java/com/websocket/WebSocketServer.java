package com.websocket;

import com.core.Context;

import java.net.URL;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;

public class WebSocketServer {
    public static void startServer(Context ctx, HttpServer server) throws Exception {
        server.getListener("grizzly").registerAddOn(new WebSocketAddOn());
        final WebSocketApplication wbApp = new WhiteboarderApplication(ctx);
        WebSocketEngine.getEngine().register("/ws", "/session", wbApp);
        server.start();
    }
}
