package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.utils.Messanger;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class GameManager {

    private static MVWorldManager worldManager = BlackBedWars.mvCore.getMVWorldManager();

    private static HashMap<String, Game> games = new HashMap<String, Game>(); //world name -> Game
    private static HashMap<UUID, GameType> settingNpc = new HashMap<UUID, GameType>(); //player's uuid (that's setting npc) -> GameType
    private static ArrayList<UUID> removingNpc = new ArrayList<UUID>(); //player's uuid (that's removing npc)
    private static HashMap<UUID, TeamType> settingBed = new HashMap<UUID, TeamType>(); //player's uuid (that's setting team bed) -> team the player is setting bed to

    public static boolean createGame(String name) {
        return createGame(name, GameType.ONES);
    }

    public static boolean createGame(String name, GameType type) {
        if(games.containsKey(name)) {
            return false;
        } else {
            boolean created = worldManager.addWorld(name, Environment.NORMAL, Long.toString(new Random().nextLong()), WorldType.FLAT, false, "VoidGenerator");
            if(created) {
                games.put(name, new Game(name, worldManager.getMVWorld(name), type));
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean removeGame(String name) {
        if(games.containsKey(name)) {
            games.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public static boolean importGame(String name) {
        return importGame(name, GameType.ONES);
    }

    public static boolean importGame(String name, GameType type) {
        if(games.containsKey(name)) {
            return false;
        } else {
            if(!worldManager.isMVWorld(name)) worldManager.loadWorld(name);
            MultiverseWorld world = worldManager.getMVWorld(name);
            if(world == null) return false;
            games.put(name, new Game(name, world, type));
        }
        return true;
    }

    public static String joinGame(GameType type, Player player) {
        return joinGame(type, new Player[] { player });
    }

    public static String joinGame(GameType type, Player[] players) {

        if(players.length > type.getNumeric()) return "Twoja drużyna nie może dołączyć do tego typu gry";

        // Filtering and sorting games

        if(GameManager.getGames().values().isEmpty()) return "Wystąpił błąd w dołączaniu do gry, zgłoś to administrowi"; // no games found at all
        List<Game> games = Arrays.asList(Arrays.stream(GameManager.getGames().values().toArray())
            .filter(game -> ((Game) game).getType().equals(type)) // only wanted game type
            .filter(game -> !((Game) game).isFull()) // only unfull games
            .toArray(Game[]::new)
        );
        
        if(games.isEmpty()) return "Nie znaleziono wolnej gry";

        Collections.sort(games, new Comparator<Game>() {

            @Override
            public int compare(Game o1, Game o2) {
                return o2.getPlayersCount() - o1.getPlayersCount();
            }

        });

        // Choosing matching game

        Game pickedGame = null;
        Team pickedTeam = null;
        Comparator<Team> teamComparator = new Comparator<Team>() {

            @Override
            public int compare(Team o1, Team o2) {
                return o1.getMembers().size() - o2.getMembers().size();
            }
            
        };

        for(Game game : games) {

            List<Team> teams = new ArrayList<Team>(game.teams.getTeams());
            teams = Arrays.asList(teams.stream().filter(team -> type.getNumeric() - team.getMembers().size() >= players.length).toArray(Team[]::new));

            if(!teams.isEmpty()) {
                Collections.sort(teams, teamComparator);
                pickedGame = game;
                pickedTeam = teams.get(0);
                break;
            }

        }

        if(pickedGame == null || pickedTeam == null) return "Nie znaleziono gry wolnej";

        // Joining player to mathing game

        for(Player player : players) {

            if(pickedGame.addPlayer(player)) {
                if(pickedTeam.addMember(player.getUniqueId())) {
                    player.teleport(pickedGame.getSpawn());
                    pickedGame.broadcastMessage(ChatColor.GOLD + player.getName() + " dołączył do gry");
                } else {
                    Messanger.sendWarning(player, "Nie możesz dołączyć do tej rozgrywki, gdyż już w niej uczestniczysz");
                }
            } else {
                Messanger.sendWarning(player, "Błąd przy dołączaniu do rozgrywki");
            }

        }

        return null;
    }

    public static void resetGames() {
        for(Game game : games.values()) {
            if(game.isRunning()) game.stopGame();
            game.killArmorStands();
        }
    }

    public static HashMap<String, Game> getGames() {
        return games;
    }

    @SuppressWarnings("unchecked")
    public static void setGames(HashMap<?, ?> games) {
        GameManager.games = (HashMap<String, Game>) games;
    }

    public static void setSettingNpc(HashMap<UUID, GameType> settingSign) {
        GameManager.settingNpc = settingSign;
    }

    //functions

    public static boolean presentGame(World world) {
        return games.containsKey(world.getName());
    }

    public static boolean presentGame(String name) {
        return games.containsKey(name);
    }

    public static boolean presentGame(UUID uuid) {
        return settingNpc.containsKey(uuid);
    }

    public static Game getGame(World world) {
        return games.get(world.getName());
    }

    public static Game getGame(String name) {
        return games.get(name);
    }

    public static GameType getSettingGameType(Player player) {
        return settingNpc.get(player.getUniqueId());
    }

    public static GameType getSettingGameType(UUID uuid) {
        return settingNpc.get(uuid);
    }

    public static boolean removingNpc(Player player) {
        return removingNpc.contains(player.getUniqueId());
    }

    public static boolean removingNpc(UUID uuid) {
        return removingNpc.contains(uuid);
    }

    public static boolean settingNpc(Player player) {
        return settingNpc.containsKey(player.getUniqueId());
    }

    public static boolean settingNpc(UUID uuid) {
        return settingNpc.containsKey(uuid);
    }
    
    public static boolean settingBed(Player player) {
        return settingBed.containsKey(player.getUniqueId());
    }

    public static boolean settingBed(UUID uuid) {
        return settingBed.containsKey(uuid);
    }

    public static TeamType getSettingBedTeam(Player player) {
        return settingBed.get(player.getUniqueId());
    }

    public static TeamType getSettingBedTeam(UUID uuid) {
        return settingBed.get(uuid);
    }

    public static void addRemovingNpc(Player player) {
        removingNpc.add(player.getUniqueId());
    }

    public static void addRemovingNpc(UUID uuid) {
        removingNpc.add(uuid);
    }

    public static void addSettingNpc(Player player, GameType type) {
        settingNpc.put(player.getUniqueId(), type);
    }

    public static void addSettingNpc(UUID uuid, GameType type) {
        settingNpc.put(uuid, type);
    }

    public static void addSettingBed(Player player, TeamType type) {
        settingBed.put(player.getUniqueId(), type);
    }

    public static void addSettingBed(UUID uuid, TeamType type) {
        settingBed.put(uuid, type);
    }

    public static void removeRemovingNpc(Player player) {
        removingNpc.remove(player.getUniqueId());
    }

    public static void removeRemovingNpc(UUID uuid) {
        removingNpc.remove(uuid);
    }

    public static void removeSettingNpc(Player player) {
        settingNpc.remove(player.getUniqueId());
    }

    public static void removeSettingNpc(UUID uuid) {
        settingNpc.remove(uuid);
    }

    public static void removeSettingBed(Player player) {
        settingBed.remove(player.getUniqueId());
    }

    public static void removeSettingBed(UUID uuid) {
        settingBed.remove(uuid);
    }
}