package com.blackmc.blackbedwars.core;

public enum ItemSpawnerType {

    BASIC,
    DIAMOND,
    EMERALD;

    public static ItemSpawnerType getByName(String name) {
        try {
            return ItemSpawnerType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
