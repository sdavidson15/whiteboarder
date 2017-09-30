package com.model;

import java.util.Date;
import java.util.UUID;

public class Image {

    public final int MAX_WB_NAME_LENGTH = 32;
    
    private String imgID;
    private String wbID;
    private String filename;
    private byte[] bytes;
    private Date date;

    public Image(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;

        this.id = UUID.randomUUID().toString();
        this.date = new Date();  
    }

    public String getID() {
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