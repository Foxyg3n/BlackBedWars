package com.blackmc.blackbedwars.listeners;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangeWorldEvent implements Listener {
    
    public ChangeWorldEvent(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {

        //player left world event
        World from = e.getFrom();

        if(GameManager.presentGame(from)) {

        }

        //player joined world event
        World to = e.getPlayer().getWorld();

        if(GameManager.presentGame(to)) {

        }

    }

}