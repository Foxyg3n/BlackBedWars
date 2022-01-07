package com.blackmc.blackbedwars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.blackmc.blackbedwars.commands.GameCmd;
import com.blackmc.blackbedwars.commands.ItemSpawnerCmd;
import com.blackmc.blackbedwars.commands.LeaveGameCmd;
import com.blackmc.blackbedwars.commands.PartyCmd;
import com.blackmc.blackbedwars.commands.ResetWorldCmd;
import com.blackmc.blackbedwars.commands.TeamJoinCmd;
import com.blackmc.blackbedwars.commands.TeamLeaveCmd;
import com.blackmc.blackbedwars.commands.TestCmd;
import com.blackmc.blackbedwars.commands.TestCmd2;
import com.blackmc.blackbedwars.commands.TestCmd3;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.GameType;
import com.blackmc.blackbedwars.core.PartyMember;
import com.blackmc.blackbedwars.core.Teams;
import com.blackmc.blackbedwars.database.BBWDatabase;
import com.blackmc.blackbedwars.database.DatabaseManager;
import com.blackmc.blackbedwars.hooks.papi.BBWExpansion;
import com.blackmc.blackbedwars.hooks.skript.BedWarsEventValues;
import com.blackmc.blackbedwars.listeners.BreakEvent;
import com.blackmc.blackbedwars.listeners.JoinEvent;
import com.blackmc.blackbedwars.listeners.LeaveEvent;
import com.blackmc.blackbedwars.listeners.NPCClickEvt;
import com.blackmc.blackbedwars.listeners.PlaceEvent;
import com.blackmc.blackbedwars.listeners.PlayerDeathEvt;
import com.blackmc.blackbedwars.listeners.PlayerRespawnEvt;
import com.blackmc.blackbedwars.listeners.PreventExplosion;
import com.blackmc.blackbedwars.listeners.RightClickEvent;
import com.blackmc.blackbedwars.utils.DataManager;
import com.google.gson.reflect.TypeToken;
import com.onarandombox.MultiverseCore.MultiverseCore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.tade.quickboard.api.QuickBoardAPI;

public class BlackBedWars extends JavaPlugin {

    public static BlackBedWars instance;
    public static MultiverseCore mvCore;
    public static QuickBoardAPI boardAPI;
    public static SkriptAddon skriptAddon;
    public static boolean shuttingDown = false;

    DataManager dataManager;

    @Override
    public void onEnable() {

        //Initialization
        instance = this;
        mvCore = getPlugin(MultiverseCore.class);
        dataManager = new DataManager();
        DatabaseManager.registerDatabaseConnection();

        Teams.initBukkitTeams();

        //DataManagement
        try {
            GameManager.setGames(dataManager.fileToHashMap("games", new TypeToken<HashMap<String, Game>>() {}.getType()));
            HashMap<GameType, ArrayList<Integer>> npcs = dataManager.fileToHashMap("npcs", new TypeToken<HashMap<GameType, ArrayList<Integer>>>() {}.getType());
            if(npcs != null) {
                GameType.hashMapToNpc(npcs);
            }
        } catch(IOException e) {
            Bukkit.getLogger().warning("Nie udało się w pełni wczytać danych z plików");
        }

        //Plugin Integration
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new BBWExpansion().register();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (getServer().getPluginManager().getPlugin("Skript") != null) {
            getLogger().info("Skript was found! Registering BlackBedWars skript expansion...");
            skriptAddon = Skript.registerAddon(this);
            try {
                skriptAddon.loadClasses("com.blackmc.blackbedwars.hooks.skript", "classes");
                skriptAddon.loadClasses("com.blackmc.blackbedwars.hooks.skript", "conditions");
                skriptAddon.loadClasses("com.blackmc.blackbedwars.hooks.skript", "effects");
                skriptAddon.loadClasses("com.blackmc.blackbedwars.hooks.skript", "events");
                skriptAddon.loadClasses("com.blackmc.blackbedwars.hooks.skript", "expressions");
                new BedWarsEventValues();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getLogger().info("Skript addon for BlackBedWars registered!");
        } else {
            getLogger().warning("Could not find Skript! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        if (Bukkit.getPluginManager().getPlugin("QuickBoard") == null) {
            getLogger().warning("Could not find QuickBoard! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        
        if (Bukkit.getPluginManager().getPlugin("Citizens") == null) {
            getLogger().warning("Could not find Citizens! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Commands
        new GameCmd(this);
        new ResetWorldCmd(this);
        new ItemSpawnerCmd(this);
        new TeamJoinCmd(this);
        new TeamLeaveCmd(this);
        new LeaveGameCmd(this);
        new TestCmd(this);
        new TestCmd2(this);
        new TestCmd3(this);
        new PartyCmd(this);

        //Events
        new NPCClickEvt(this);
        new RightClickEvent(this);
        new PlayerDeathEvt(this);
        new PlaceEvent(this);
        new BreakEvent(this);
        new JoinEvent(this);
        new LeaveEvent(this);
        new PreventExplosion(this);
        new PlayerRespawnEvt(this);

        //Games restoration
        ArrayList<Game> stoppedGames = new ArrayList<Game>();
        ArrayList<Game> brokenGames = new ArrayList<Game>();

        for(Game game : GameManager.getGames().values()) {
            if(!game.shouldInit())
                stoppedGames.add(game);
            else
                if(!game.init()) brokenGames.add(game);
        }

        if(!stoppedGames.isEmpty()) Bukkit.getLogger().info("Znaleziono " + stoppedGames.size() + " zatrzymanych gier");
        for(Game game : stoppedGames) {
            Bukkit.getLogger().info("- " + game.getName());
        }
        
        if(!brokenGames.isEmpty()) Bukkit.getLogger().info("\033[31m" + "Znaleziono " + brokenGames.size() + " uszkodzonych gier" + "\033[0m");
        for(Game game : brokenGames) {
            Bukkit.getLogger().info("\033[31m" + "- " + game.getName() + "\033[0m");
        }

        //Logging
        Bukkit.getLogger().info("Siema, jestem Twoim pluginem BedWars");
        
    }

    public static void main(String[] args) {
        DatabaseManager.registerDatabaseConnection();
        BBWDatabase database = BBWDatabase.getDatabase();
        UUID uuid = UUID.randomUUID();
        database.getOrCreateParty(uuid);
        PartyMember member = database.getPartyMember(uuid);
        System.out.println(member.getUUID());
        System.out.println(member.getParty());
    }

    @Override
    public void onDisable() {
        shuttingDown = true;
        
        GameManager.resetGames();

        try {
            HashMap<GameType, ArrayList<Integer>> npcs = GameType.npcToHashMap();
            dataManager.hashMapToFile("games", GameManager.getGames());
            dataManager.hashMapToFile("npcs", npcs);
        } catch(IOException e) {
            Bukkit.getLogger().warning("Nie udało się w pełni zapisać danych do plików");
        }
    
        Bukkit.getLogger().info("Pa pa, serwer się wyłącza to i ja się wyłączam, narka");
        
    }

}