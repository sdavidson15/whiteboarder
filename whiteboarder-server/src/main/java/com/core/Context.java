package com.core;

import com.db.DatabaseConnector;
import com.model.User;

public class Context {
    private User user;
    private DatabaseConnector dbc;
    private boolean isLocal;

    public Context(User user, DatabaseConnector dbc) {
        this.user = user;
        this.dbc = dbc;
    }

    public User user() {
        return this.user;
    }

    public DatabaseConnector dbc() {
        return this.dbc;
    }

    public boolean isLocal() {
        return this.isLocal;
    }
}