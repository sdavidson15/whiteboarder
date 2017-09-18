package com.model;

import java.util.Date;
import javax.sql.rowset.serial.SerialBlob;

public class Image {
    
    private String id;
    private String filename;
    private SerialBlob bytes; // Possibly don't need this here? We could grab the binary only when we need it, instead of attaching it to this.
    private Date date;

    public Image(String id, String filename, SerialBlob bytes) {
        this.id = id;
        this.filename = filename;
        this.bytes = bytes; // Again, probably don't need to do this.

        this.date = new Date();  
    }

    public String getId() {
        return this.id;
    }

    public String getFilename() {
        return this.filename;
    }

    public SerialBlob getBytes() {
        return this.bytes;
    }

    public Date getDate() {
        return this.date;
    }

    public static Image CreateImage(String id, String filename, SerialBlob data) {
        // This might be a better strategy
        Image img = new Image(id, filename, null);
        // Persist the data which is retrievable by id
        return img;
    }
}