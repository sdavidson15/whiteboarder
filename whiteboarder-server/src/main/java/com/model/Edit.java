package com.model;

import java.awt.Point;
import java.util.Date;
import java.util.Set;

public class Edit {

    private int color;
    private Set<Point> points;
    private int brushSize;
    private int userID;
    private Date date;

    public Edit(int color, Set<Point> points, int brushSize, int userID) {
        this.color = color;
        this.brushSize = brushSize;
        this.userID = userID;
        this.points = points;

        this.date = new Date();
    }

    public int getColor() {
        return this.color;
    }

    public Set<Point> getPoints() {
        return this.points;
    }

    public int getBrushSize() {
        return this.brushSize;
    }

    public int getUserID() {
        return this.userID;
    }

    public Date getDate() {
        return this.date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Edit other = (Edit)o;
        boolean datesMatch = (this.date == null && other.getDate() == null) || (this.date != null && this.date.equals((other.date)));
        return datesMatch && other.userID == this.userID;
    }
}