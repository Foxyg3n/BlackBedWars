package com.blackmc.blackbedwars.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.GameType;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.md_5.bungee.api.ChatColor;

public class NPCClickEvt implements Listener {
    
    public NPCClickEvt(BlackBedWars plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlace(NPCRightClickEvent e) {

        int npcId = e.getNPC().getId();
        Player player = e.getClicker();

        if(GameManager.settingNpc(player)) {

            GameType gameType = GameManager.getSettingGameType(player);
            boolean addedNpc = gameType.addNpc(npcId);

            if(addedNpc) {
                Messanger.sendConfirm(player, "Prawidłowo przypisano NPC (id:" + ChatColor.YELLOW + npcId + ChatColor.GREEN + ") do typu gier: " + gameType);
            } else {
                Messanger.sendWarning(player, "Błąd w przypisywaniu NPC (id:" + ChatColor.YELLOW + npcId + ChatColor.RESET + ") do typu gier: " + gameType + ChatColor.YELLOW + "(prawdopodobnie ten npc jest już przypisany)");
            }

            GameManager.removeSettingNpc(player);

        } else if(GameManager.removingNpc(player)) {

            boolean removedNpc = GameType.removeNpc(npcId);

            if(removedNpc) {
                Messanger.sendConfirm(player, "Prawidłowo usunięto przypisanie NPC (id:" + ChatColor.YELLOW + npcId + ChatColor.GREEN + ")");
            } else {
                Messanger.sendWarning(player, "Błąd w usuwaniu przypisania NPC (id:" + ChatColor.YELLOW + npcId + ChatColor.RED + ")" + ChatColor.YELLOW + " (prawdopodobnie ten npc nie był przypisany)");
            }

            GameManager.removeRemovingNpc(player);

        } else {

            HashMap<GameType, ArrayList<Integer>> npcs = GameType.npcToHashMap();
            GameType type = null;

            for(Entry<GameType, ArrayList<Integer>> entry : npcs.entrySet()) {
                for(Integer id : entry.getValue()) {
                    if(id == npcId) {
                        type = entry.getKey();
                        break;
                    }
                }
            }

            if(type == null) return;

            String error = GameManager.joinGame(type, player);

            if(error != null) Messanger.sendWarning(player, error);

        }

    }

}