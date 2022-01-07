package com.blackmc.blackbedwars.listeners;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.utils.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnEvt implements Listener {
    
    public PlayerRespawnEvt(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRevive(PlayerRespawnEvent e) {

        Player player = e.getPlayer();
        Game game = GameManager.getGame(player.getWorld().getName());

        if(game != null) {
            if(game.isStopped()) {
                e.setRespawnLocation(WorldManager.getInitialWorld().getSpawnLocation());
            } else {
                Team team = game.getTeam(player);
                if(team != null) {
                    e.setRespawnLocation(team.getSpawn());
                } else {
                    e.setRespawnLocation(game.getSpawn());
                    Bukkit.broadcastMessage("Odradzanie jako spectator");
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
        } else {
            e.setRespawnLocation(WorldManager.getInitialWorld().getSpawnLocation());
        }

    }

}