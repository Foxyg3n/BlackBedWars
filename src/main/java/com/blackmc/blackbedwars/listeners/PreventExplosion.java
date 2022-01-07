package com.blackmc.blackbedwars.listeners;

import java.util.Iterator;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class PreventExplosion implements Listener {

    public PreventExplosion(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {

        Bukkit.broadcastMessage("TNT wybuchło");

        Game game = GameManager.getGame(e.getEntity().getWorld());

        if(game != null) {

            for(Iterator<Block> iterator = e.blockList().iterator(); iterator.hasNext();) {

                Block block = iterator.next();

                if(!game.hasBlock(block)) {
                    iterator.remove();
                    Bukkit.broadcastMessage("Usunięto blok z listy niszczonych bloków");
                }

            }

        }

    }
    
}