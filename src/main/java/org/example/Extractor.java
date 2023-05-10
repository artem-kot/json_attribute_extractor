package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;

public class Extractor {

    public void jsonAttributeExtractor(String attribute, String filePath) {
        Gson gson = new Gson();
        File file = new File(filePath);
        String json = null;
        try {
            json = Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        System.out.println(jsonObject.get(attribute));
    }

    public static void main(String[] args) {
        Extractor e = new Extractor();
        e.jsonAttributeExtractor("structured", "src/main/resources/1.json");
    }
}