package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * This class is intended to collect the utterances from the dialogs stored in json files.
 * We have several folders (test, dev, train). Each folder contains files with non-unique names.
 * Each file contain dialogs with non-unique id's.
 *
 * Output of the main method should be a text file, which contains bank-related dialogs from
 * all the files in all 3 directories.
 *
 * To make it we have to:
 * 1. Collect all the file names, create some sort of the map for navigation.
 * Collect file names from directory to some datatype as String values. If file name doesn't have
 * .json extension, go deeper and collect child items of this file.
 * 2. Once we have a map of all the files, we should filter them by the service.Since we should
 * only focus on collecting bank dialogs, we can parse each json dialog and check the "service"
 * parameter value. If it's not bank related, we shouldn't use it. If the whole file doesn't
 * contain a single bank related dialog, we remove the whole file.
 * 3. Now when we have only files with bank related dialogs, we should use another datatype to
 * read id of those dialogs, combine them with filenames and save as separate custom objects.
 * 4. Dialog object should have unique id field and array of Strings to store the utterances.
 * 5. Once dialog objects are in place, we should create a few options to write them into
 * json / txt files.
 *
 *
 */

public class Extractor {

    private static final String parentDirectory = "src/main/resources/";
    private static final String targetExtension = ".json";

    private ArrayList<String> getFilesFromDir(String directory) {
        File dir = new File(directory);
        List<File> files = Arrays.asList(Objects.requireNonNull(dir.listFiles()));

        ArrayList<String> result = new ArrayList<>();
        for (File file : files) {
            result.add(file.getName());
        }
        return result;
    }

    public ArrayList<String> createFilesLibrary() {
        ArrayList<String> library = null;
        ArrayList<String> dirs = getFilesFromDir(parentDirectory);

        /**
         * method implementation, generates a big library of existing folders and files.
         * each String value contains file name and parent dirs.
         */

        return library;
    }

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

    public static void main(String[] args) {
        Extractor e = new Extractor();
        System.out.println(e.getFilesFromDir(parentDirectory));
    }
}