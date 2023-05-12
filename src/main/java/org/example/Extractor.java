package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Extractor {

    public void extractJsonFieldFromFile(String attribute, String filePath) {
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

    public File[] collectFileNames(String parentDirectory) {
        File directory = new File(parentDirectory);
        return directory.listFiles();
    }

    public static void main(String[] args) {
        Extractor e = new Extractor();
        e.extractJsonFieldFromFile("sample", "src/main/resources/1.json");

        File file = new File("src/main/resources/");
        System.out.println(Arrays.toString(file.listFiles()));
    }
}