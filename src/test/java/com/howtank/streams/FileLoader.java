package com.howtank.streams;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public class FileLoader {
    private Class clazz;
    public FileLoader(Class clazz) {
        this.clazz = clazz;
    }

    public String loadFile(String fileName) {
        URL url = Resources.getResource(clazz, fileName);
        try {
            String data = Resources.toString(url, Charsets.UTF_8);
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load a JSON file.", e);
        }
    }
}
