package com.blackmc.blackbedwars.listeners;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent implements Listener {
    
    public PlaceEvent(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        /* if(e.getPlayer().hasBypass()) return; */ 

        Game game = GameManager.getGame(e.getPlayer().getWorld());

        if(game != null) {

            if(game.isRunning()) {
            
                game.addBlock(e.getBlock());
    
            }

        }

    }

}