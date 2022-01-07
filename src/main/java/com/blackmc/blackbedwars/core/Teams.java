package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class Teams {

    private HashMap<String, Team> teams = new HashMap<String, Team>();
    private HashMap<UUID, String> players = new HashMap<UUID, String>();

    public Teams(TeamSize size) {
        switch(size) {
        case EIGHT_TEAMS:
            teams.put(TeamType.RED.name(), new Team(TeamType.RED));
            teams.put(TeamType.GREEN.name(), new Team(TeamType.GREEN));
            teams.put(TeamType.BLUE.name(), new Team(TeamType.BLUE));
            teams.put(TeamType.YELLOW.name(), new Team(TeamType.YELLOW));
            teams.put(TeamType.PINK.name(), new Team(TeamType.PINK));
            teams.put(TeamType.GRAY.name(), new Team(TeamType.GRAY));
            teams.put(TeamType.WHITE.name(), new Team(TeamType.WHITE));
            teams.put(TeamType.AQUA.name(), new Team(TeamType.AQUA));
            break;
        case FOUR_TEAMS:
            teams.put(TeamType.RED.name(), new Team(TeamType.RED));
            teams.put(TeamType.GREEN.name(), new Team(TeamType.GREEN));
            teams.put(TeamType.BLUE.name(), new Team(TeamType.BLUE));
            teams.put(TeamType.YELLOW.name(), new Team(TeamType.YELLOW));
            break;
        default:
            break;
        }

    }

    public static void initBukkitTeams() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for(TeamType type : TeamType.values()) {
            org.bukkit.scoreboard.Team team = scoreboard.getTeam(type.name().toLowerCase());
            if(team == null) scoreboard.registerNewTeam(type.name().toLowerCase());
        }
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

    public Team getTeam(TeamType type) {
        return teams.get(type.name());
    }

    public Team getTeam(Player player) {
        for(Team team : teams.values()) {
            if(team.isMember(player.getUniqueId())) return team;
        }
        return null;
    }

    public boolean hasTeam(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public boolean addMember(TeamType type, Player player) {
        boolean result = teams.get(type.name()).addMember(player.getUniqueId());
        if(result) players.put(player.getUniqueId(), type.name());
        return result;
    }

    public boolean removeMember(Player player) {
        Team team = getTeam(player);
        if(team == null) return false;
        boolean result = team.removeMember(player.getUniqueId());
        if(result) players.remove(player.getUniqueId());
        return result;
    }

    public void removeMembers(TeamType type, ArrayList<Player> players) {
        for(Player player : players) {
            teams.get(type.name()).removeMember(player.getUniqueId());
            this.players.remove(player.getUniqueId());
        }
    }

    public void removeMembers(TeamType type) {
        teams.get(type.name()).removeMembers();
        for(Entry<UUID, String> entry : players.entrySet()) {
            if(entry.getValue().equals(type.name())) players.remove(entry.getKey());
        }
    }

    public void removeMembers() {
        for(Team team : teams.values()) {
            team.removeMembers();
        }
        players.clear();
    }

}