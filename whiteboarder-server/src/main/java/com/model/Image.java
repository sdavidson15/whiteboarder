package com.model;

import java.util.Date;

public class Image {
    
    private String id;
    private String filename;
    private byte[] bytes;
    private Date date;

    public Image(String id, String filename, byte[] bytes) {
        this.id = id;
        this.filename = filename;
        this.bytes = bytes;

        this.date = new Date();  
    }

    public String getId() {
        return this.id;
    }

    public String getFilename() {
        return this.filename;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public Date getDate() {
        return this.date;
    }
}