package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Whiteboard {

    public final int MAX_WB_NAME_LENGTH = 32;

    private String wbID;
    private String name;
    private List<Image> imageHistory;
    private List<Edit> edits;

    public Whiteboard(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
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
        if (img == null || img.getBytes() == null || img.getBytes().length == 0) {
            return;
        }

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