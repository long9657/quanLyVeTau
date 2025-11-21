package com.app.qlvetau.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    @SuppressWarnings("unchecked")
    public static <T> List<T> readFromFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<T>) obj;
            } else {
                return new ArrayList<>();
            }
        } catch (EOFException eof) {
            // empty file -> return empty list
            return new ArrayList<>();
        } catch (Exception e) {
            // print minimal trace for debugging; in production replace with logger
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void writeToFile(String filePath, List<T> list) throws IOException {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(list);
            oos.flush();
        }
    }
}