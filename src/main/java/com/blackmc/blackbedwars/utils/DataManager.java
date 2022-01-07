package com.blackmc.blackbedwars.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.ItemSpawner;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.core.adapters.GameAdapter;
import com.blackmc.blackbedwars.core.adapters.ItemSpawnerAdapter;
import com.blackmc.blackbedwars.core.adapters.LocationAdapter;
import com.blackmc.blackbedwars.core.adapters.TeamAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Location;

public class DataManager {

    public final File ROOT;
    private Gson gson = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(Game.class, new GameAdapter())
        .registerTypeAdapter(ItemSpawner.class, new ItemSpawnerAdapter())
        .registerTypeAdapter(Location.class, new LocationAdapter())
        .registerTypeAdapter(Team.class, new TeamAdapter())
        .create();

    public DataManager() {
        ROOT = BlackBedWars.instance.getDataFolder();
        if(!ROOT.exists()) ROOT.mkdir();
    }

    public <K, V> void hashMapToFile(String filename, HashMap<K, V> map) throws IOException {

        filename = filename.endsWith(".json") ? filename : filename + ".json";
        File file = new File(ROOT, filename);
        if(!file.exists()) file.createNewFile();
        saveFile(file, gson.toJson(map));

    }

    public <K, V> HashMap<K, V> fileToHashMap(String filename, Type type) throws IOException {

        filename = filename.endsWith(".json") ? filename : filename + ".json";

        File file = new File(ROOT, filename);
        String content = loadFile(file);
        if(content != null)
            return gson.fromJson(content, type);
        else
            return new HashMap<K, V>();

    }

    private void saveFile(File file, String content) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(utf8(content));
        out.close();
    }

    private String loadFile(File file) throws IOException {

        int length = (int) file.length();
        byte[] output = new byte[length];
        FileInputStream in = new FileInputStream(file);
        int offset = 0;
        while(offset < length)  {
            offset += in.read(output, offset, (length - offset));
        }
        in.close();
        return utf8(output);
        
    }

    private byte[] utf8(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    private String utf8(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}