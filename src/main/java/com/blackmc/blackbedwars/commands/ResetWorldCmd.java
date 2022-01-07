package com.blackmc.blackbedwars.commands;

import com.blackmc.blackbedwars.BlackBedWars;
import com.onarandombox.MultiverseCore.MultiverseCore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetWorldCmd implements CommandExecutor {

    public ResetWorldCmd(BlackBedWars plugin) {
        plugin.getCommand("resetworld").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        MultiverseCore multiverseCore = MultiverseCore.getPlugin(MultiverseCore.class);

        if(sender instanceof Player) {

            Bukkit.unloadWorld("game1", false);
            multiverseCore.getMVWorldManager().loadWorld("game1");

        }

        return true;
    }
    
}