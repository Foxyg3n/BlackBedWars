package com.blackmc.blackbedwars.commands;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.utils.WorldManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameCmd implements CommandExecutor {

    public LeaveGameCmd(BlackBedWars plugin) {
        plugin.getCommand("opusc").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            
            Player player = (Player) sender;
            Game game = GameManager.getGame(player.getWorld().getName());
            if(game != null) game.removePlayer(player);
            player.teleport(WorldManager.getInitialWorld().getSpawnLocation()); //temp

        }

        return true;
    }
    
}
