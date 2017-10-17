package com.core;

import com.db.DatabaseConnector;
import com.model.User;

public class Context {
    private User user;
    private DatabaseConnector dbc;
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

    public boolean isLocal() {
        return this.isLocal;
    }

    public boolean isValid() {
        return this.dbc != null;
    }
}