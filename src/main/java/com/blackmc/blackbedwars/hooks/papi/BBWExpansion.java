package com.blackmc.blackbedwars.hooks.papi;

import java.util.concurrent.TimeUnit;

import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.core.TeamType;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;

public class BBWExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "Foxyg3n";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bbw";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(player != null && player.isOnline()) {
            Game game = GameManager.getGame(player.getPlayer().getWorld().getName());

            if(game == null) return null;

            if(params.equalsIgnoreCase("playerteam")) {

                Team team = game.getTeam(player.getPlayer());
                if(team != null) return team.getType().getChatColor() + team.getType().name() + ChatColor.RESET;

            } else if(params.equalsIgnoreCase("game_players")) {

                return "" + game.getPlayers().size();

            } else if(params.startsWith("teamstate")) {

                TeamType type = TeamType.getByName(params.substring(params.indexOf("_") + 1));
                if(type != null) {
                    Team team = game.getTeam(type);
                    if(team != null) return (team.isBedDestroyed() ? (team.getMembers().isEmpty() ? ChatColor.RED + "✗" : "" + team.getMembers().size()) : ChatColor.GREEN + "✔");
                }

            } else if(params.startsWith("game_time")) {

                int timer = game.getTimer();
                long durationMillis = TimeUnit.SECONDS.toMillis(timer);
                return DurationFormatUtils.formatDuration(durationMillis, "mm:ss");

            } else if(params.startsWith("game_state")) {

                return (!game.isInitialized() ?  ChatColor.RED + "Niezainicjalizowana" : (game.getStartingTask() == null ? "Oczekiwanie..." : "" + game.getStartingTimer()));

            } else if(params.startsWith("teamname")) {

                Team team = game.getTeam(player.getPlayer());
                if(team != null) return team.getType().getDisplayName();
                
            }
        }
        return null;
    }
    
}

