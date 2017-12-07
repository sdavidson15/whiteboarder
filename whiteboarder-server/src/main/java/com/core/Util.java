package com.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Util is the class responsible for housing all the common utility methods.
 * @author Stephen Davidson
 */
public class Util {

    /**
     * fileToByteArr converts an object of type File to a byte array.
     * @param File the file to be converted.
     * @return a byte array corresponding to the provided File.
     */
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