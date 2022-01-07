package com.blackmc.blackbedwars.utils;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Messanger {

    // Send message to a player
 
    public static void sendMessage(Player player, String text) {
        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + text);
    }
 
    public static void sendWarning(Player player, String text) {
        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + text);
    }
 
    public static void sendInfo(Player player, String text) {
        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE + text);
    }
 
    public static void sendConfirm(Player player, String text) {
        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + text);
    }

    // Send message to multiple players
 
    public static void sendMessage(Player[] players, String text) {
        for(Player player : players) {
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + text);
        }
    }
 
    public static void sendWarning(Player[] players, String text) {
        for(Player player : players) {
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + text);
        }
    }
 
    public static void sendInfo(Player[] players, String text) {
        for(Player player : players) {
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE + text);
        }
    }
 
    public static void sendConfirm(Player[] players, String text) {
        for(Player player : players) {
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Black" + ChatColor.WHITE + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + text);
        }
    }

}