package com.blackmc.blackbedwars.commands;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamLeaveCmd implements CommandExecutor {

    public TeamLeaveCmd(BlackBedWars plugin) {
        plugin.getCommand("leave").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;
            Game game = GameManager.getGame(player.getWorld());
        
            if(game.removeTeamMember(player))
                Messanger.sendConfirm(player, "Wyszedłeś z teamu: " + game.getTeam(player).getType().name().toLowerCase());
            else
                Messanger.sendWarning(player, "Obecnie nie znajdujesz się w tej drużynie");

        }

        return true;
    }
    
}