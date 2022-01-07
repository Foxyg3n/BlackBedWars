package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import ch.njol.util.Pair;

public class Team {

    private TeamType type;
    private ArrayList<UUID> members;
    private Location spawnpoint;
    private ItemSpawner furnace;
    private Entry<Location, Location> bed;
    private boolean bedDestroyed;
    private org.bukkit.scoreboard.Team bukkitTeam;

    public Team(TeamType type) {
        this.type = type;
        this.members = new ArrayList<UUID>();
        this.spawnpoint = null;
        this.furnace = null;
        this.bedDestroyed = false;
        this.bed = new Pair<Location, Location>();
        this.bukkitTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(type.name().toLowerCase());
    }

    public TeamType getType() {
        return type;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public Location getSpawn() {
        return spawnpoint;
    }

    public Entry<Location, Location> getBed() {
        return bed;
    }

    public ItemSpawner getFurnace() {
        return furnace;
    }

    public void setSpawn(Location spawnpoint) {
        this.spawnpoint = spawnpoint;
    }

    public void setBed(Pair<Location, Location> pair) {
        this.bed = pair;
    }

    public void setBed(Location loc1, Location loc2) {
        this.bed = new Pair<Location,Location>(loc1, loc2);
    }

    public void setFurnace(ItemSpawner furnace) {
        this.furnace = furnace;
    }

    public boolean hasBed() {
        return bed != null;
    }

    public void removeBed() {
        this.bed = null;
    }

    //functions

    @SuppressWarnings("deprecation")
    public boolean addMember(UUID uuid) {
        boolean result = members.add(uuid);
        if(result) bukkitTeam.addPlayer(Bukkit.getPlayer(uuid));
        return result;
    }

    @SuppressWarnings("deprecation")
    public boolean removeMember(UUID uuid) {
        boolean result = members.remove(uuid);
        if(result) bukkitTeam.removePlayer(Bukkit.getPlayer(uuid));
        return result;
    }

    @SuppressWarnings("deprecation")
    public void removeMembers() {
        for(UUID uuid : members) {
            bukkitTeam.removePlayer(Bukkit.getPlayer(uuid));
        }
        members.clear();
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public boolean isBedDestroyed() {
        return bedDestroyed;
    }

    public void setBedDestroyed(boolean destroyed) {
        this.bedDestroyed = destroyed;
    }

    public void destroyBed() {
        bed.getKey().getBlock().setType(Material.AIR);
        bed.getValue().getBlock().setType(Material.AIR);
        setBedDestroyed(true);
    }

}