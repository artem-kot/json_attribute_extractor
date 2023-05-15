package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
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
 */

public class Extractor {

    private static final String parentDirectory = "src/main/resources/";
    private static final String targetExtension = ".json";
    private static final String targetService = "banks";
    private static final String targetOutputFile = parentDirectory + "output.txt";
    public static ArrayList<String> library = new ArrayList<>();
    public static HashMap<String, ArrayList<String>> dialogs = new HashMap<>();

    private File[] getFilesFromDir(String directory) {
        return new File(directory).listFiles();
    }

    private void libraryFilesCollector(File[] files) {
        for (File file : files) {
            if (file.getName().endsWith(targetExtension)) {
                library.add(file.getPath());
            } else {
                libraryFilesCollector(getFilesFromDir(parentDirectory + file.getName()));
            }
        }
    }

    public void updateLibrary() {
        library.clear();
        libraryFilesCollector(getFilesFromDir(parentDirectory));
    }

    public void printLibrary() {
        System.out.printf("Library contains %s elements.\n", library.size());
//        for (String file : library) {
//            System.out.println(file);
//        }
    }

    public void filterLibrary() {
        System.out.println("Filtering only bank-related dialogs");
        for (int i = 0; i < library.size(); i++) {
            if (!isTargetServiceInTheFile(library.get(i))) {
                library.remove(i);
            }
        }
    }

    public void printDialogs() {
        for (Map.Entry entry : dialogs.entrySet()) {
            System.out.println(entry);
        }
    }

    public boolean isTargetServiceInTheFile(String filePath) {
        Gson gson = new Gson();
        File file = new File(filePath);
        String json = null;
        try {
            json = Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        JsonArray bodyArray = gson.fromJson(json, JsonArray.class);

        for (JsonElement jo : bodyArray) {
            JsonObject bodyObject = jo.getAsJsonObject();
            String service = bodyObject.get("services").toString();
            if (service.toLowerCase().contains(targetService)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> getUtterances(JsonArray turns) {
        ArrayList<String> utterances = new ArrayList<>();
        for (JsonElement turn : turns) {
            JsonObject jo = turn.getAsJsonObject();
            utterances.add(jo.get("utterance").toString());
        }
        return utterances;
    }

    public void extractDialogsFromFile(String filePath) {
        Gson gson = new Gson();
        File file = new File(filePath);
        String json = null;
        try {
            json = Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        JsonArray bodyArray = gson.fromJson(json, JsonArray.class);
        for (JsonElement jo : bodyArray) {
            JsonObject bodyObject = jo.getAsJsonObject();
            String service = bodyObject.get("services").toString();
            if (service.toLowerCase().contains(targetService)) {
                JsonArray turns = bodyObject.get("turns").getAsJsonArray();
                String dialogId = filePath + "_" + bodyObject.get("dialogue_id").getAsString();
                ArrayList<String> utterances = getUtterances(turns);
                dialogs.put(dialogId, utterances);
            }
        }
    }

    public void extractAllDialogs() {
        for (String file : library) {
            extractDialogsFromFile(file);
        }
    }

    public void writeDialogsToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetOutputFile, true));
            writer.write("dialogs output:\n\n");
            for (Map.Entry entry : dialogs.entrySet()) {
                writer.append(entry.getKey().toString()).append("\n");
                ArrayList<String> utterances = dialogs.get(entry.getKey().toString());
                for (String utterance : utterances) {
                    writer.append(utterance).append("\n");
                }
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Extractor e = new Extractor();
        e.updateLibrary();
        e.printLibrary();
        e.filterLibrary();
        e.printLibrary();
        e.extractAllDialogs();
        e.writeDialogsToFile();
    }
}