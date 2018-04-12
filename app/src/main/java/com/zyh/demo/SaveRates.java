package com.zyh.demo;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/10
 */

public class SaveRates {

    private static String defaultPath = "/sdcard/damily_cache/";

    public void writeObject(Object object, String name, Context context)
            throws FileNotFoundException, IOException {
        File file = getFile(name, context);
        ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(file));
        outputStream.writeObject(object);
        outputStream.flush();
        outputStream.close();
    }

    public Object readObject(String name, Context context) throws FileNotFoundException,
            IOException, ClassNotFoundException {
        File file = getFile(name, context);
        ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream(file));
        Object object = inputStream.readObject();
        inputStream.close();
        return object;
    }

    private File getFile(String name, Context context) {
        File path = new File(defaultPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String objPath = path.getAbsolutePath() + name + ".txt";
        File file = new File(objPath);
        if (file.exists()) {
            return file;
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}