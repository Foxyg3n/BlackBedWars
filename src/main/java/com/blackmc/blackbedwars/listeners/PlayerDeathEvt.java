package com.blackmc.blackbedwars.listeners;

import java.util.Arrays;
import java.util.List;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.utils.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class PlayerDeathEvt implements Listener {
    
    public PlayerDeathEvt(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    BukkitTask task;

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {

        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            
            if((player.getHealth() - e.getFinalDamage()) <= 0) {
                e.setCancelled(true);

                Game game = GameManager.getGame(player.getWorld().getName());

                if(game != null) {
                    Team team = game.getTeam(player);

                    player.getInventory().clear();
                
                    if(player.getKiller() != null) {
                        Player killer = player.getKiller();
                        game.broadcastMessage(player.getName() + " został zabity przez " + killer.getName());

                        List<ItemStack> contents = Arrays.asList(player.getInventory().getContents());
                        ItemStack[] mathingItems = contents.stream().filter(item -> item.getType().equals(Material.DIAMOND) || item.getType().equals(Material.EMERALD)).toArray(ItemStack[]::new);

                        for(ItemStack item : mathingItems) {
                            killer.getInventory().addItem(item);
                        }

                    } else {
                        game.broadcastMessage(player.getName() + " zginął");
                    }

                    if(team == null) {
                        player.teleport(WorldManager.getInitialWorld().getSpawnLocation());
                        return;
                    }

                    if(team.isBedDestroyed()) {
                        game.removeTeamMember(player);
                        player.teleport(game.getSpectatorSpawn());
                        player.setGameMode(GameMode.SPECTATOR);
                    } else {
                        respawnInGame(game, player);
                    }
                    
                    if(game.lastTeamLeft()) game.stopGame();

                } else {
                    respawnInLobby(player);
                }

            }

        }

    }

    private void respawnInLobby(Player player) {
        player.teleport(WorldManager.getInitialWorld().getSpawnLocation());
        player.setHealth(20);
    }

    private void respawnInGame(Game game, Player player) {
        player.teleport(game.getSpectatorSpawn());
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(20);

        if(game.isRunning()) task = Bukkit.getScheduler().runTaskTimer(BlackBedWars.instance, new Runnable() {

            int counter = 5;

            @Override
            public void run() {

                if(!game.isRunning()) task.cancel();

                if(counter == 5)
                player.sendTitle(ChatColor.GOLD + "5", "", 5, 10, 5);
                else if(counter == 4)
                player.sendTitle(ChatColor.GOLD + "4", "", 5, 10, 5);
                else if(counter == 3)
                    player.sendTitle(ChatColor.GOLD + "3", "", 5, 10, 5);
                else if(counter == 2)
                player.sendTitle(ChatColor.GOLD + "2", "", 5, 10, 5);
                else if(counter == 1)
                player.sendTitle(ChatColor.GOLD + "1", "", 5, 10, 5);
                else if(counter == 0) {
                    player.teleport(game.getTeam(player).getSpawn());
                    player.setGameMode(GameMode.SURVIVAL);
                    task.cancel();
                }

                counter--;
                
            }
            
        }, 20, 20);
        
    }

}