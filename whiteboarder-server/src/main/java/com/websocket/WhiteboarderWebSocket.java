package com.websocket;

import java.util.logging.Logger;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DefaultWebSocket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketListener;

/**
 * WhiteboarderWebSocket is the web socket implementation for Whiteboarder.
 * @author Stephen Davidson.
 */
public class WhiteboarderWebSocket extends DefaultWebSocket {
    private volatile String sessionID;
    private volatile String user;

    public WhiteboarderWebSocket(ProtocolHandler protocolHandler, HttpRequestPacket request,
            WebSocketListener... listeners) {
        super(protocolHandler, request, listeners);
    }

    /**
     * @return the id of the session corresponding to this web socket.
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * @return the requesting user.
     */
    public String getUser() {
        return user;
    }

    /**
     * @param sessionID the session id to be set.
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @param user the user to be set.
     */
    public void setUser(String user) {
        this.user = user;
    }
}