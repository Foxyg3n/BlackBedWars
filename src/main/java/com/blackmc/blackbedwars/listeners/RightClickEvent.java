package com.blackmc.blackbedwars.listeners;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.utils.BedUtils;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RightClickEvent implements Listener {

    public RightClickEvent(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void signClickEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(BedUtils.isBedBlock(block)) {
                if(GameManager.settingBed(player)) {
                
                    Bed bed = (Bed) block.getBlockData();
                    Block head = null;
                    Block feet = null;

                    if(BedUtils.isBedHead(block)) {
                        head = block;
                        feet = block.getRelative(bed.getFacing().getOppositeFace());
                    } else {
                        head = block.getRelative(bed.getFacing());
                        feet = block;
                    }

                    Game game = GameManager.getGame(player.getWorld().getName());

                    if(game != null) {
                        Team team = game.getTeam(GameManager.getSettingBedTeam(player));
                        team.setBed(head.getLocation(), feet.getLocation());
                        GameManager.removeSettingBed(player);
                        Messanger.sendConfirm(player, "Prawidłowo przypisałeś łóżko drużynie " + team.getType().getDeclinedName());
                    } else {
                        Messanger.sendWarning(player, "Łóżko musi znajdować się na świecie z grą");
                    }

                    e.setCancelled(true);
                        
                }
            }
        }

    }

}