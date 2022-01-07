package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public enum GameType {
    ONES(1, "1v1"),
    DOUBLES(2, "2v2"),
    TRIPPLES(3, "3v3"),
    QUADRUPLES(4, "4v4");

    private ArrayList<Integer> npcs = new ArrayList<Integer>();
    private int numeric;
    private String displayName;

    GameType(int numeric, String displayName) {
        this.numeric = numeric;
        this.displayName = displayName;
    }

    public static GameType getByName(String name) {
        try {
            return GameType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static HashMap<GameType, ArrayList<Integer>> npcToHashMap() {

        HashMap<GameType, ArrayList<Integer>> npcHashMap = new HashMap<GameType, ArrayList<Integer>>();

        for(GameType type : GameType.values()) {
            npcHashMap.put(type, type.getNpcs());
        }

        return npcHashMap;

    }

    public static void hashMapToNpc(HashMap<GameType, ArrayList<Integer>> hashMap) {

        if(hashMap.isEmpty()) return;

        for(Entry<GameType, ArrayList<Integer>> entry : hashMap.entrySet()) {
            entry.getKey().setNpcs(entry.getValue());
        }

    }

    public int getNumeric() {
        return numeric;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean addNpc(Integer id) {
        if(npcs.contains(id)) return false;
        return npcs.add(id);
    }

    public static boolean removeNpc(Integer id) {
        for(GameType type : values()) {
            for(Integer npcId : type.npcs) {
                if(npcId == id) return type.npcs.remove(id);
            }
        }
        return false;
    }

    public ArrayList<Integer> getNpcs() {
        return npcs;
    }

    public void setNpcs(ArrayList<Integer> npcs) {
        this.npcs = npcs;
    }

}
