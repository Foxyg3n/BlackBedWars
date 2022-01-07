package com.blackmc.blackbedwars.listeners;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    public LeaveEvent(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        
        Player player = e.getPlayer();
        Game game = GameManager.getGame(player.getWorld());

        if(game != null) {

            game.removePlayer(player);

        }

    }
    
}
