package com.uvtech.makerwala.utilities;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptDecrypt {

    public static void encode(String sourceFile, String targetFile) throws Exception {
        byte[] base64EncodedData = Base64.encode(loadFileAsBytesArray(sourceFile), Base64.DEFAULT);
        writeByteArraysToFile(targetFile, base64EncodedData);
    }

    public static void decode(String sourceFile, String targetFile) throws Exception {
        byte[] decodedBytes = Base64.decode(loadFileAsBytesArray(sourceFile), Base64.DEFAULT);
        writeByteArraysToFile(targetFile, decodedBytes);
    }

    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {
        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;
    }

    public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException {
        File file = new File(fileName);
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }

}
