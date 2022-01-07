package com.blackmc.blackbedwars.commands;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.TeamType;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class TeamJoinCmd implements CommandExecutor {

    public TeamJoinCmd(BlackBedWars plugin) {
        plugin.getCommand("join").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(args.length == 0) {

                Messanger.sendWarning(player, "Wprowadź więcej argumentów");

            } else if(args.length == 1) {

                Game game = GameManager.getGame(player.getWorld());
                TeamType type = TeamType.getByName(args[0]);

                if(game != null && type != null) {
            
                    if(game.getTeam(player) != null) game.removeTeamMember(player);
                    if(game.addTeamMember(type, player))
                        Messanger.sendMessage(player, ChatColor.GOLD + "Dołączyłeś do drużyny " + type.getDeclinedName());
                    else
                        Messanger.sendWarning(player, "Nie możesz dołączyć do tej drużyny");

                } else {
                    
                    Messanger.sendWarning(player, "Nie znaleziono gry lub drużyny");

                }

            }

        }

        return true;
    }
    
}