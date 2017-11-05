package com.websocket;

import java.util.logging.Logger;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DefaultWebSocket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketListener;

public class WhiteboarderWebSocket extends DefaultWebSocket {
    private volatile String sessionID;
    private volatile String user;

    public WhiteboarderWebSocket(ProtocolHandler protocolHandler, HttpRequestPacket request,
            WebSocketListener... listeners) {
        super(protocolHandler, request, listeners);
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getUser() {
        return user;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setUser(String user) {
        this.user = user;
    }
}