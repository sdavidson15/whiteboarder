package com.model;

/**
 * Model class Point is an xy coordinate belonging to an Edit.
 * @author Stephen Davidson
 */
public class Point {
    private int editID;
    private int xcoord;
    private int ycoord;

    /**
     * Class constructor.
     * @param editID id of this point's edit.
     * @param xcoord x coordinate.
     * @param ycoord y coordinate.
     */
    public Point(int editID, int xcoord, int ycoord) {
        this.editID = editID;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    /**
     * @return this point's edit id.
     */
    public int getEditID() {
        return this.editID;
    }

    /**
     * @param editID edit id to be set.
     */
    public void setEditID(int editID) {
        this.editID = editID;
    }

    /**
     * @return this point's x coordinate.
     */
    public int getXCoord() {
        return this.xcoord;
    }

    /**
     * @param xcoord x coordinate to be set.
     */
    public void setXCoord(int xcoord) {
        this.xcoord = xcoord;
    }

    /**
     * @return this points' y coordinate.
     */
    public int getYCoord() {
        return this.ycoord;
    }

    /**
     * @param ycoord y coordinate to be set.
     */
    public void setYCoord(int ycoord) {
        this.ycoord = ycoord;
    }
}