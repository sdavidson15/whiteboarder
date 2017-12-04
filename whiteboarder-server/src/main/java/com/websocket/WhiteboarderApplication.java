package com.websocket;

import com.core.Context;
import com.core.Manager;
import com.core.WbException;
import com.core.Logger;
import com.model.Edit;
import com.model.Message;
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
        Logger.log.info("Creating a web socket.");
        return new WhiteboarderWebSocket(handler, request, listeners);
    }

    @Override
    public void onMessage(WebSocket websocket, String jsonData) {
        if (jsonData != null && jsonData.startsWith("login:")) {
            login(websocket, jsonData);
            return;
        }

        if (jsonData != null && jsonData.startsWith("message:")) {
            handleMessage(websocket, jsonData);
            return;
        }

        handleEdit(websocket, jsonData);
    }

    @Override
    public void onConnect(WebSocket websocket) {
        // Have to override this and do nothing.
    }

    @Override
    public void onClose(WebSocket websocket, DataFrame frame) {
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        Logger.log.info(wws.getUser() + " left session " + wws.getSessionID());
        try {
            Manager.removeUser(ctx, wws.getSessionID(), wws.getUser());
        } catch (WbException e) {
            Logger.log.severe("Error while removing User: " + e.getMessage());
        }
        members.remove(websocket);
    }

    public void refreshImage(String sessionID) {
        broadcast("refreshImage:" + sessionID);
    }

    public void refreshUsers(String sessionID) {
        broadcast("refreshUsers:" + sessionID);
    }

    private void login(WebSocket websocket, String jsonData) {
        // Expect to recieve a login message that looks like "login:{sessionID},{username}"
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        String[] fields = jsonData.substring(6).split(",");
        if (fields.length != 2) {
            Logger.log.warning("Recieved web socket login message with bad syntax: " + jsonData);
            return;
        }
        String sessionID = fields[0];
        String username = fields[1];

        try {
            Manager.addUser(ctx, sessionID, username);
        } catch (WbException e) {
            Logger.log.severe("Error while adding User: " + e.getMessage());
            return;
        }

        wws.setSessionID(sessionID);
        wws.setUser(username);
        members.add(websocket);
        Logger.log.info(wws.getUser() + " joined session " + wws.getSessionID());
        refreshUsers(sessionID);
    }

    private void handleEdit(WebSocket websocket, String jsonData) {
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        if (wws.getSessionID() == null || wws.getUser() == null)
            return;

        Gson gson = new GsonBuilder().create();
        HandleEdit h = null;
        try {
            h = gson.fromJson(jsonData, new TypeToken<HandleEdit>() {
            }.getType());
            h.edit.setNewTimestamp();
        } catch (JsonSyntaxException e) {
            Logger.log.warning("Recieved web socket message with bad json syntax.");
            return;
        }

        Logger.log.info("Recieved web socket message to handle an Edit.");
        try {
            if (h.isRemove)
                Manager.removeEdit(ctx, h.edit);
            else {
                h.edit = Manager.applyEdit(ctx, h.edit);
                jsonData = gson.toJson(h);
            }
        } catch (WbException e) {
            Logger.log.severe("Error while handling edit: " + e.getMessage());
            return;
        }

        Logger.log.info("User " + wws.getUser() + " in session " + wws.getSessionID() + " is broadcasting an Edit.");
        broadcast(jsonData);
    }

    private void handleMessage(WebSocket websocket, String jsonData) {
        WhiteboarderWebSocket wws = (WhiteboarderWebSocket) websocket;
        String jsonMsg = jsonData.substring(8);
        if (wws.getSessionID() == null || wws.getUser() == null)
            return;

        Gson gson = new GsonBuilder().create();
        Message m = null;
        try {
            m = gson.fromJson(jsonMsg, new TypeToken<Message>() {
            }.getType());
            m.setNewTimestamp();
        } catch (JsonSyntaxException e) {
            Logger.log.warning("Recieved web socket message with bad json syntax.");
            return;
        }

        Logger.log.info("Recieved web socket message to handle a Message object.");
        try {
            // unable to remove messages at this time
            m = Manager.sendMessage(ctx, m);
            jsonData = gson.toJson(m);
        } catch (WbException e) {
            Logger.log.severe("Error while handling Message: " + m.getMessage());
            return;
        }

        Logger.log.info("User " + wws.getUser() + " in session " + wws.getSessionID() + " is broadcasting a Message.");
        broadcast("message:" + jsonData);
    }

    private void broadcast(String jsonData) {
        broadcaster.broadcast(members, jsonData);
    }
}