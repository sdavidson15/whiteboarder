package com.websocket;

import com.core.Context;

import java.net.URL;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;

/**
 * WebSocketServer is class responsible for registering the web socket application and serving
 * the web socket.
 * @author Stephen Davidson
 */
public class WebSocketServer {

    /**
     * startServer regiester the whiteboarder web socket application and starts the server.
     * @param ctx the server context.
     * @param server the rest server (api and static file handler).
     */
    public static void startServer(Context ctx, HttpServer server) throws Exception {
        final WebSocketApplication wbApp = new WhiteboarderApplication(ctx);
        ctx.setWbApp((WhiteboarderApplication) wbApp);

        server.getListener("grizzly").registerAddOn(new WebSocketAddOn());
        WebSocketEngine.getEngine().register("/ws", "/session", wbApp);
        server.start();
    }
}
