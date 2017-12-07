package com.core;

import com.db.DatabaseConnector;
import com.model.User;
import com.websocket.WhiteboarderApplication;

/**
 * Context allows access to server dependencies by housing references to them.
 * A single Context instance exists on the main thread, and is passed around as needed.
 * @author Stephen Davidson
 */
public class Context {
    private User user;
    private DatabaseConnector dbc;
    private WhiteboarderApplication wbApp;
    private boolean isLocal;

    /**
     * Class constructor.
     * @param user user making the server request.
     * @param dbc database connector instance.
     * @param isLocal whether or not the server is being run locally.
     */
    public Context(User user, DatabaseConnector dbc, boolean isLocal) {
        this.user = user;
        this.dbc = dbc;
        this.isLocal = isLocal;
    }

    /**
     * @return User that is making the current server request.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * @return connection to the database.
     */
    public DatabaseConnector getDbc() {
        return this.dbc;
    }

    /**
     * @return the whiteboarder web socket application.
     */
    public WhiteboarderApplication getWbApp() {
        return this.wbApp;
    }

    /**
     * @param wbApp whiteboarder web socket application to set.
     */
    public void setWbApp(WhiteboarderApplication wbApp) {
        this.wbApp = wbApp;
    }

    /**
     * @return whether or not the server is running locally.
     */
    public boolean isLocal() {
        return this.isLocal;
    }

    /**
     * @return whether or not the server context is valid.
     */
    public boolean isValid() {
        return this.dbc != null;
    }
}