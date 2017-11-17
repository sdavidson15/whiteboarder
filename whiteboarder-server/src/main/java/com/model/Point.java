package com.model;

public class Point {
    private int editID;
    private int xcoord;
    private int ycoord;

    public Point(int editID, int xcoord, int ycoord) {
        this.editID = editID;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    public int getEditID() {
        return this.editID;
    }

    public void setEditID(int editID) {
        this.editID = editID;
    }

    public int getXCoord() {
        return this.xcoord;
    }

    public void setXCoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYCoord() {
        return this.ycoord;
    }

    public void setYCoord(int ycoord) {
        this.ycoord = ycoord;
    }
}