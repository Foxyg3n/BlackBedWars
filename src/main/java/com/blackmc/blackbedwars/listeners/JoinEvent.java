package com.blackmc.blackbedwars.listeners;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.utils.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    public JoinEvent(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        World lobby = WorldManager.getInitialWorld();

        player.teleport(lobby.getSpawnLocation());

    }
    
}