package com.model;

public class User {
    public enum Mode {
        HOST, COLLABORATOR, VIEWER;
    }

    private String name;
    private int id;
    private Mode mode;

    public User(String name, int id, Mode mode) {
        this.name = name;
        this.id = id;
        this.mode = mode;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String newName) {
        this.name = newName;
    }

    public int getID() {
        return this.id;
    }

    public Mode getMode() {
        return this.mode;
    }
    public void setMode(Mode newMode) {
        this.mode = newMode;
    }
}