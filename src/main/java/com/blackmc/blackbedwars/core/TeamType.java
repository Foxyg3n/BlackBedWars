package com.blackmc.blackbedwars.core;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public enum TeamType {
    RED(Material.RED_BED, ChatColor.RED, "czerwonych", "Czerwoni"),
    GREEN(Material.GREEN_BED, ChatColor.GREEN, "zielonych", "Zieloni"),
    BLUE(Material.BLUE_BED, ChatColor.BLUE, "niebieskich", "Niebiescy"),
    YELLOW(Material.YELLOW_BED, ChatColor.YELLOW, "żółtych", "Żółci"),
    PINK(Material.PINK_BED, ChatColor.LIGHT_PURPLE, "różowych", "Różowi"),
    GRAY(Material.GRAY_BED, ChatColor.GRAY, "szarych", "Szarzy"),
    WHITE(Material.WHITE_BED, ChatColor.WHITE, "białych", "Biali"),
    AQUA(Material.CYAN_BED, ChatColor.AQUA, "aqua", "Aqua");

    private Material bedType;
    private ChatColor color;
    private String declinedName;
    private String displayName;

    private TeamType(Material material, ChatColor color, String declinedName, String displayName) {
        this.bedType = material;
        this.color = color;
        this.declinedName = declinedName;
        this.displayName = color + displayName;
    }

    public static TeamType getByName(String name) {
        try {
            return TeamType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Material getBedType() {
        return bedType;
    }
    
    public ChatColor getChatColor() {
        return color;
    }

    public String getDeclinedName() {
        return declinedName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
}