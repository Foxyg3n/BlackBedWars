package com.blackmc.blackbedwars.listeners;

import java.util.UUID;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.core.TeamType;

import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.md_5.bungee.api.ChatColor;

public class BreakEvent implements Listener {
    
    public BreakEvent(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Game game = GameManager.getGame(e.getPlayer().getWorld());

        if(game != null) {

            if(game.isRunning()) {

                if(e.getBlock().getBlockData() instanceof Bed) {

                    TeamType type = game.getBedTeam(e.getBlock().getLocation());
        
                    if(type != null) {

                        if(type != game.getTeam(e.getPlayer()).getType()) {
                    
                            if(e.isDropItems()) e.setDropItems(false);

                            Team team = game.getTeam(type);
                            team.setBedDestroyed(true);
                            for(UUID uuid : team.getMembers()) {
                                Player player = Bukkit.getPlayer(uuid);
                                if(player != null) player.sendTitle(ChatColor.DARK_RED + "Łóżko zniszczone!", "", 10, 30, 10);
                                game.broadcastMessage(team.getType().getChatColor() + "Łóżko " + team.getType().getDeclinedName() + " zostało zniszczone");
                            }
                            
                        } else {
                            e.setCancelled(true);
                        }

                    }

                } else {
            
                    if(game.hasBlock(e.getBlock())) {
                        game.removeBlock(e.getBlock());
                    } else {
                        e.setCancelled(true);
                    }

                }

            }

        }

    }

}