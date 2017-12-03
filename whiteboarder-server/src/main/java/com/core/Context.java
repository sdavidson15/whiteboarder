package com.core;

import com.db.DatabaseConnector;
import com.model.User;
import com.websocket.WhiteboarderApplication;

public class Context {
    private User user;
    private DatabaseConnector dbc;
    private WhiteboarderApplication wbApp;
    private boolean isLocal;

    public Context(User user, DatabaseConnector dbc, boolean isLocal) {
        this.user = user;
        this.dbc = dbc;
        this.isLocal = isLocal;
    }

    public User getUser() {
        return this.user;
    }

    public DatabaseConnector getDbc() {
        return this.dbc;
    }

    public WhiteboarderApplication getWbApp() {
        return this.wbApp;
    }

    public void setWbApp(WhiteboarderApplication wbApp) {
        this.wbApp = wbApp;
    }

    public boolean isLocal() {
        return this.isLocal;
    }

    public boolean isValid() {
        return this.dbc != null;
    }
}