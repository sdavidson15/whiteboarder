package com.websocket;

import com.model.Edit;

import java.awt.Frame;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.Broadcaster;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.OptimizedBroadcaster;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketListener;

public class WhiteboarderApplication extends WebSocketApplication {
    // This class exists for JSON marshalling/unmarshalling
    class WhiteboarderMessage {
        String sessionID;
        String username;
        Edit edit;

        WhiteboarderMessage(String sessionID, String username, Edit edit) {
            this.sessionID = sessionID;
            this.username = username;
            this.edit = edit;
        }
    }

    private Set<WebSocket> members = Collections.newSetFromMap(new ConcurrentHashMap<WebSocket, Boolean>());
    private final Broadcaster Broadcaster = new OptimizedBroadcaster();

    @Override
    public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket request, WebSocketListener... listeners) {
        return new WhiteboarderWebSocket(handler, request, listeners);
    }

    @Override
    public void onMessage(WebSocket websocket, String jsonData) {
        // TODO: Parse data for a WhiteboarderMessage
        WhiteboarderMessage wbm = new WhiteboarderMessage("sessionID", "username", edit); // gson.fromJson(jsonData);
        if ()

        // TODO: Add the Edit to the database
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        broadcast(wws.getSessionID(), wws.getUser(), jsonData);
    }

    @Override
    public void onConnect(WebSocket socket) {
        // TODO: Figure out what this means
        // need to override this to take control over the members
    }

    @Override
    public void onClose(WebSocket websocket, DataFrame frame) {
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        Logger.log.info(wws.getUser() + " left session " + wws.getSessionID());
        members.remove(websocket);
    }

    private void broadcast(String sessionID, String username, Edit edit) {
        Logger.log.info("User " + username + " in session " + sessionID + " is broadcasting an Edit.");
        // TODO: Convert the edit to json
        String editJson = "New edit from " + username + " in session " + sessionID;
        broadcaster.broadcast(members, editJson);
    }
}