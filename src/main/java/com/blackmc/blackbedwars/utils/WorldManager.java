package com.blackmc.blackbedwars.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.blackmc.blackbedwars.BlackBedWars;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldManager {

    public final static String ROOT = StringUtils.removeEnd(BlackBedWars.instance.getServer().getWorldContainer().getAbsolutePath(), ".");
    private static World world = null;
 
    public static World getInitialWorld() {

        if(world != null) {
            return world;
        } else {
            try {
                File file = new File(ROOT + "server.properties");
                Scanner sc = new Scanner(file);
    
                String phrase = "level-name";
                World world = null;
            
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    int index = line.indexOf("=");
                    String regex = index != -1 ? line.substring(0, line.indexOf("=")) : "";
                    if (regex.equals(phrase)) {
                        String worldName = StringUtils.removeStart(line, phrase + "=");
                        world = Bukkit.getWorld(worldName);
                    }
                }
                sc.close();
    
                return world;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    
            return null;
            
        }

    }

}