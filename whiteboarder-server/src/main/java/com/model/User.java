package com.model;

public class User {
    public enum Mode {
        HOST(0), COLLABORATOR(1), VIEWER(2);

        private final int value;
        private Mode(int value) {
            this.value = value;
        }
    
        public int getValue() {
            return value;
        }
    }

    public final int MAX_USER_NAME_LENGTH = 32;

    private String wbID;
    private String username;
    private Mode mode;

    public User(String wbID, String name, Mode mode) {
        this.wbID = wbID;
        this.name = name;
        this.mode = mode;
    }

    public int getWbID() {
        return this.wbID;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String newName) {
        this.name = newName;
    }

    public Mode getMode() {
        return this.mode;
    }
    public void setMode(Mode newMode) {
        this.mode = newMode;
    }
}