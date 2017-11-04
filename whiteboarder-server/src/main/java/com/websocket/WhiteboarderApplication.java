package com.websocket;

import com.core.Context;
import com.core.Manager;
import com.core.Logger;
import com.model.Edit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.awt.Frame;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

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
    // Internal class for JSON serialization
    class HandleEdit {
        Edit edit;
        boolean isRemove;

        public HandleEdit(Edit edit, boolean isRemove) {
            this.edit = edit;
            this.isRemove = isRemove;
        }
    }

    private Context ctx;

    public WhiteboarderApplication(Context ctx) {
        super();
        this.ctx = ctx;
    }

    private Set<WebSocket> members = Collections.newSetFromMap(new ConcurrentHashMap<WebSocket, Boolean>());
    private final Broadcaster broadcaster = new OptimizedBroadcaster();

    @Override
    public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket request, WebSocketListener... listeners) {
        return new WhiteboarderWebSocket(handler, request, listeners);
    }

    @Override
    public void onMessage(WebSocket websocket, String jsonData) {
        HandleEdit h = null;
        try {
            Gson gson = new GsonBuilder().create();
            h = gson.fromJson(jsonData, new TypeToken<HandleEdit>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            Logger.log.warning("Recieved web socket message with bad json syntax.");
            // TODO: Handle error
        }

        if (h.isRemove)
            Manager.removeEdit(ctx, h.edit);
        else
            Manager.applyEdit(ctx, h.edit);

        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        broadcast(wws.getSessionID(), wws.getUser(), jsonData);
    }

    @Override
    public void onConnect(WebSocket websocket) {
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        Manager.addUser(ctx, wws.getSessionID(), wws.getUser());
        Logger.log.info(wws.getUser() + " joined session " + wws.getSessionID());
    }

    @Override
    public void onClose(WebSocket websocket, DataFrame frame) {
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        Logger.log.info(wws.getUser() + " left session " + wws.getSessionID());
        Manager.removeUser(ctx, wws.getSessionID(), wws.getUser());
        members.remove(websocket);
    }

    private void broadcast(String sessionID, String username, String jsonData) {
        Logger.log.info("User " + username + " in session " + sessionID + " is broadcasting an Edit.");
        broadcaster.broadcast(members, jsonData);
    }
}