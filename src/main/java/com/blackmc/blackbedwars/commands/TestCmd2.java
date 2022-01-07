package com.blackmc.blackbedwars.commands;

import java.util.UUID;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Party;
import com.blackmc.blackbedwars.database.BBWDatabase;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCmd2 implements CommandExecutor {

    public TestCmd2(BlackBedWars plugin) {
        plugin.getCommand("test2").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            BBWDatabase database = BBWDatabase.getDatabase();
            Player player = (Player) sender;
            Party party = database.getParty(player);
            System.out.println("Party: " + party);
            if(party != null) for(UUID uuid : party.getMembers()) {
                System.out.println(" - " + Bukkit.getPlayer(uuid).getName() + "(" + uuid + ")");
            }

        }

        return true;
    }
    
}