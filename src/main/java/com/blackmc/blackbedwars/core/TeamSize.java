package com.blackmc.blackbedwars.core;

public enum TeamSize {
    FOUR_TEAMS(4),
    EIGHT_TEAMS(8);

    private int size;

    TeamSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
