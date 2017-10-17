package com.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {

    public static byte[] inputToByteArr(InputStream input) throws WbException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];

        try {
            int numBytes = input.read(buffer);
            while ((numBytes = input.read(buffer)) != -1)
                output.write(buffer, 0, numBytes);
        } catch (IOException e) {
            WbException wbe = new WbException("Failed to convert input stream to byte array.", e);
            Logger.log.severe(wbe.getMessage());
            throw wbe;
        }

        return output.toByteArray();
    }
}