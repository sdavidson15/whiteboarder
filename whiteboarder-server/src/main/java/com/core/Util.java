package com.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Util {

    public static byte[] fileToByteArr(File file) throws WbException {
        byte[] output = new byte[(int) file.length()];

        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(output);
            stream.close();
        } catch (IOException e) {
            throw new WbException("Failed to convert file to byte array.", e);
        }
        return output;
    }
}