package com.model;

import java.util.List;
import java.util.ArrayList;

public class Whiteboard {

    private String uuid;
    private String name;
    private List<Image> imageHistory;
    private List<Edit> edits;

    public Whiteboard(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUUID() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
    public void setName(String newName) {
        this.name = newName;
    }

    public Image getCurrentImage() {
        if (this.imageHistory == null || this.imageHistory.size() == 0) {
            return null;
        }
        return this.imageHistory.get(0);
    }
    public List<Image> getImageHistory() {
        return this.imageHistory;
    }
    public void addImage(Image img) {
        // TODO: Do some validation on the image

        if (this.imageHistory == null) {
            this.imageHistory = new ArrayList<Image>();
        }

        this.imageHistory.add(0, img);
    }

    public Edit getMostRecentEdit() {
        if (this.edits == null || this.edits.size() == 0) {
            return null;
        }
        return this.edits.get(0);
    }
    public List<Edit> getEdits() {
        return this.edits;
    }
    public void addEdit(Edit edit) {
        if(this.edits == null) {
            this.edits = new ArrayList<Edit>();
        }

        this.edits.add(edit);
    }
    public Edit removeEdit(Edit edit) {
        if(this.edits == null || edit == null) {
            return null;
        }

        boolean removed = this.edits.remove(edit);
        return removed ? edit : null;
    }
}