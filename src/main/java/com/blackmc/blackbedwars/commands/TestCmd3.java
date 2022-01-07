package com.blackmc.blackbedwars.commands;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.FriendList;
import com.blackmc.blackbedwars.database.BBWDatabase;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCmd3 implements CommandExecutor {

    public TestCmd3(BlackBedWars plugin) {
        plugin.getCommand("test3").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        BBWDatabase database = BBWDatabase.getDatabase();

        if(sender instanceof Player) {
            Player player = (Player) sender;
            FriendList friendList = database.getOrCreateFriendList(player);

            if(args.length == 0) {
                Messanger.sendWarning(player, "Daj tam więcej trochę tych argumentów, co?");
            } else if(args.length == 1) {
                String name = args[0];
                Player friend = Bukkit.getPlayer(name);

                if(friend != null) {
                    friendList.addFriend(friend.getUniqueId());
                } else {
                    Messanger.sendWarning(player, "Nie ma takiego gracza");
                }

            }

        }

        return true;
    }
    
}