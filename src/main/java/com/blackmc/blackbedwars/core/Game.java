package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.events.GameEndEvent;
import com.blackmc.blackbedwars.events.GameJoinEvent;
import com.blackmc.blackbedwars.events.GameStartEvent;
import com.blackmc.blackbedwars.utils.BedUtils;
import com.blackmc.blackbedwars.utils.Messanger;
import com.blackmc.blackbedwars.utils.WorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class Game {

    private String name;
    private MultiverseWorld world;
    private GameState state;
    private GameType type;
    private boolean shouldInit;
    private int clock;
    private int startingTimer;
    private int maxTeamSize;
    private int playerLimit;
    private int startLimit;
    private Location spectatorSpawn;
    private ArrayList<UUID> players;
    private ArrayList<Location> blocks;
    private ArrayList<ItemSpawner> itemSpawners;
    private HashMap<Entry<Location, Location>, TeamType> beds;
    private int clockTask;
    private BukkitTask startingTask;
    private Board scoreboard;
    public Teams teams;
 
    public Game(String name, MultiverseWorld world) {
        this(name, world, GameType.ONES);
    }

    public Game(String name, MultiverseWorld world, GameType gameType) {
        this.name = name;
        this.world = world;
        this.state = GameState.STOPPED;
        this.type = gameType;
        this.shouldInit = false;
        this.clock = 0;
        this.startingTimer = 30;
        this.maxTeamSize = gameType.getNumeric();
        this.playerLimit = (maxTeamSize == 1) ? 8 : maxTeamSize * 4;
        this.startLimit = (2); // 3/4 * playerLimit
        this.players = new ArrayList<UUID>(playerLimit);
        this.blocks = new ArrayList<Location>();
        this.itemSpawners = new ArrayList<ItemSpawner>();
        this.beds = new HashMap<Entry<Location, Location>, TeamType>();
        this.scoreboard = new Board(this);
        this.teams = new Teams(maxTeamSize == 1 ? TeamSize.EIGHT_TEAMS : TeamSize.FOUR_TEAMS);
        this.startingTask = null;
        this.clockTask = 0;
        world.getCBWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.getCBWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.getCBWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.getCBWorld().setGameRule(GameRule.KEEP_INVENTORY, true);
        world.getCBWorld().setGameRule(GameRule.MOB_GRIEFING, false);
        world.getCBWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        setSpawn(0, 64, 0);
    }

    /**
     * if(this.state == GameState.RUNNING) --> uninitialize / initialize through changing the variable (don't know what var yet :P)
     */

    public boolean init() {
        if(this.state == GameState.WAITING || this.state == GameState.RUNNING) return false;
        for(Team team : teams.getTeams()) {
            if(team.getSpawn() == null || team.getBed() == null || team.getFurnace() == null) return false;
            team.setBedDestroyed(false);
        }
        if(getSpectatorSpawn() == null) return false;
        this.state = GameState.WAITING;
        this.shouldInit = true;
        //updateSigns();
        return true;
    }

    public boolean uninit() {
        if(this.state == GameState.STOPPED || this.state == GameState.RUNNING) return false;
        kickPlayers(players, "Zostałeś wyrzucony ze świata ze względu na konserwację gry");
        this.state = GameState.STOPPED;
        this.shouldInit = false;
        //updateSigns();
        return true;
    }

    public boolean isInitialized() {
        return this.state != GameState.STOPPED;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Location> getBlocks() {
        return blocks;
    }

    public ArrayList<ItemSpawner> getItemSpawners() {
        return itemSpawners;
    }

    public GameState getState() {
        return state;
    }

    public GameType getType() {
        return type;
    }

    public int getTimer() {
        return clock;
    }

    public int getStartingTimer() {
        return startingTimer;
    }

    public BukkitTask getStartingTask() {
        return startingTask;
    }

    public HashMap<Entry<Location, Location>, TeamType> getBeds() {
        return beds;
    }

    public MultiverseWorld getWorld() {
        return world;
    }

    public boolean isFull() {
        return getPlayersCount() >= playerLimit;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public int getPlayerLimit() {
        return playerLimit;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemSpawners(ArrayList<ItemSpawner> itemSpawners) {
        this.itemSpawners = itemSpawners;
    }

    public void setState(GameState status) {
        this.state = status;
    }

    public void setWorld(MultiverseWorld world) {
        this.world = world;
    }

    public void setBeds(HashMap<Entry<Location, Location>, TeamType> beds) {
        this.beds = beds;
    }

    public Team getTeam(TeamType type) {
        return teams.getTeam(type);
    }

    public Team getTeam(Player player) {
        return teams.getTeam(player);
    }

    public boolean lastTeamLeft() {
        Team[] teamsLeft = teams.getTeams().stream().filter(team -> !team.getMembers().isEmpty()).toArray(Team[]::new);
        return teamsLeft.length <= 1;
    }

    public Team getLastTeamLeft() {
        for(Team team : teams.getTeams()) {
            if(!team.getMembers().isEmpty()) return team;
        }
        return null;
    }

    public void addTeamMemberRandomly(Player player) {
        ArrayList<Team> teams = new ArrayList<Team>();
        for(Team team : this.teams.getTeams()) {
            if(team.getMembers().size() < maxTeamSize) teams.add(team);
        }
        addTeamMember(teams.get(RandomUtils.nextInt(teams.size() - 1)).getType(), player);
    }

    public boolean addTeamMember(TeamType type, Player player) {
        removeTeamMember(player);
        return teams.getTeam(type).getMembers().size() < maxTeamSize ? teams.addMember(type, player) : false;
    }

    public boolean removeTeamMember(Player player) {
        return teams.removeMember(player);
    }

    public boolean hasBlock(Block block) {
        return blocks.contains(block.getLocation());
    }

    public boolean hasBlock(Location location) {
        return blocks.contains(location);
    }

    public void addBlock(Block block) {
        blocks.add(block.getLocation());
    }

    public void addBlock(Location location) {
        blocks.add(location);
    }

    public void removeBlock(Block block) {
        blocks.remove(block.getLocation());
    }

    public void removeBlock(Location location) {
        blocks.remove(location);
    }

    public boolean shouldInit() {
        return this.shouldInit;
    }

    public void shouldInit(boolean shouldInit) {
        this.shouldInit = shouldInit;
    }

    //functions

    public void startGame() {

        this.state = GameState.RUNNING;
        this.shouldInit = false;

        for(Team team : teams.getTeams()) {
            beds.put(team.getBed(), team.getType());
        }

        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null && !teams.hasTeam(player)) addTeamMemberRandomly(player);
        }

        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) scoreboard.setInGameBoard(player);
        }

        for(ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.startSpawner(this);
        }

        for(Team team : teams.getTeams()) {

            if(!team.getMembers().isEmpty()) {

                for(UUID uuid : team.getMembers()) {
    
                    Player player = Bukkit.getPlayer(uuid);
                    player.teleport(team.getSpawn());
    
                }

                team.getFurnace().startFurnace(this);

            } else {
                team.destroyBed();
            }
            
        }
        

        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));

        
        
        clockTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlackBedWars.instance, new Runnable() {
    
            @Override
            public void run() {

                clock++;
                
            }
            
        }, 20, 20);

        //updateSigns();

    }

    public void stopGame() {

        if(this.state.equals(GameState.STOPPED)) return;

        this.state = GameState.STOPPED;

        Bukkit.getPluginManager().callEvent(new GameEndEvent(this));

        Bukkit.getScheduler().cancelTask(clockTask);

        for(ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.stopSpawning();
        }

        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                clearPlayer(player);
                player.sendTitle("", "Drużyna " + getLastTeamLeft().getType().getDeclinedName() + " wygrała", 3, 60, 3);
            }
        }

        //updateSigns();

        if(!BlackBedWars.shuttingDown) {

            Bukkit.getScheduler().runTaskLater(BlackBedWars.instance, new Runnable() {
    
                @Override
                public void run() {
    
                    resetGame();
                    
                }
                
            }, 10 * 20);

        } else {
    
            resetGame();
            
        }

    }

    public void resetGame() {

        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) clearPlayer(player);
        }

        teams.removeMembers();
        players.clear();

        for(Team team : teams.getTeams()) {
            team.getFurnace().reset();
        }

        for(ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.reset();
        }

        clock = 0;

        kickPlayers();
        resetWorld();
        clearDrop();
        blocks.clear();
        init();

    }

    public void resetWorld() {
        // Bukkit.unloadWorld(world.getName(), false);
        // MultiverseCore.getPlugin(MultiverseCore.class).getMVWorldManager().loadWorld(world.getName());

        for(Location location : blocks) {
            Block block = location.getBlock();
            Chunk chunk = block.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            location.getBlock().setType(Material.AIR);
        }

        for (Team team : teams.getTeams()) {
            Entry<Location, Location> bed = team.getBed();
            BedUtils.setBed(bed.getKey().getBlock(), bed.getValue().getBlock(), team.getType().getBedType());
        }
    }

    public boolean isStopped() {
        return this.state.equals(GameState.STOPPED);
    }

    public boolean isWaiting() {
        return this.state.equals(GameState.WAITING);
    }

    public boolean isRunning() {
        return this.state.equals(GameState.RUNNING);
    }

    /*public void updateSigns() {
        for(Location location : signs) {
            BlockState block = location.getBlock().getState();
            if(block instanceof Sign) {
                Sign sign = (Sign) block;
                String[] lines = sign.getLines();
                lines[0] = ChatColor.BLACK + getName();
                lines[1] = "[" + playerLimit + "v" + playerLimit + "]";
                switch(state) {
                    case STOPPED:
                        lines[2] = ChatColor.DARK_RED + "Kończenie gry";
                        break;
                    case WAITING:
                        lines[2] = ChatColor.GREEN + "Oczekiwanie...";
                        break;
                    case RUNNING:
                    lines[2] = ChatColor.YELLOW + "W trakcie...";
                        break;
                    default:
                        break;

                }
                lines[3] = players.size() + "/" + (type.equals(GameType.ONES) ? "8" : "16");
                sign.update();
            }
        }
    }

    public void unloadSigns() {
        for(Location location : signs) {
            BlockState block = location.getBlock().getState();
            if(block instanceof Sign) {
                Sign sign = (Sign) block;
                String[] lines = sign.getLines();
                lines[1] = ChatColor.DARK_RED + "Gra wyłączona";
                sign.update();
            }
        }
    }*/

    public void kickPlayers() {
        Location lobby = WorldManager.getInitialWorld().getSpawnLocation();
                
        for(Player player : world.getCBWorld().getPlayers()) {
            player.teleport(lobby);
        }
    }

    public void kickPlayers(String message) {
        Location lobby = WorldManager.getInitialWorld().getSpawnLocation();
                
        for(Player player : world.getCBWorld().getPlayers()) {
            player.teleport(lobby);
            Messanger.sendInfo(player, message);
        }
    }

    public void kickPlayers(ArrayList<UUID> players, String message) {
        Location lobby = WorldManager.getInitialWorld().getSpawnLocation();
                
        for(UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) {
                player.teleport(lobby);
                Messanger.sendInfo(player, message);
            }
        }
    }

    public void removePlayer(Player player) {
        if(isWaiting()) checkForStart();
        removeTeamMember(player);
        players.remove(player.getUniqueId());
        broadcastMessage(ChatColor.GRAY + player.getName() + " wyszedł z gry");
        //updateSigns();
    }

    public boolean addPlayer(Player player) {
        if(players.contains(player.getUniqueId())) return false;
        Bukkit.getPluginManager().callEvent(new GameJoinEvent(player, this));
        boolean joined = players.add(player.getUniqueId());
        if(isWaiting()) checkForStart();
        //updateSigns();
        scoreboard.setLobbyBoard(player);
        return joined;
    }

    public void removeItemSpawner(ItemSpawner itemSpawner) {
        itemSpawner.stopSpawning();
        itemSpawner.armorStand.remove();
        this.itemSpawners.remove(itemSpawner);
    }

    public void killArmorStands() {
        for(ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.armorStand.remove();
        }
    }

    public void addItemSpawner(ItemSpawner itemSpawner) {
        this.itemSpawners.add(itemSpawner);
    }

    public Location getSpawn() {
        return world.getSpawnLocation();
    }

    public void setSpawn(double x, double y, double z) {
        world.setSpawnLocation(fixLocation(new Location(world.getCBWorld(), x, y, z)));
    }

    public void setSpawn(Location location) {
        world.setSpawnLocation(fixLocation(location));
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Location location) {
        this.spectatorSpawn = location;
    }

    public TeamType getBedTeam(Location location) {
        for(Entry<Location, Location> bed : beds.keySet()) {
            if(
                location.equals(bed.getKey()) ||
                location.equals(bed.getValue()))
            return beds.get(bed);
        }
        return null;
    }

    public void setTeamSpawn(Location location, TeamType type) {
        teams.getTeam(type).setSpawn(fixLocation(location));
    }

    public void clearDrop() {
        world.getCBWorld().getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove);
    }

    public void broadcastMessage(String message) {
        for(UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    private Location fixLocation(Location location) {
        int rest = (int) (Math.abs(location.getYaw()) / 45);
        float sign = Math.signum((float) location.getYaw());
        float yaw = rest == 0 ? 0 : rest == 1 || rest == 2 ? sign * 90 : 180;
        return new Location(location.getWorld(), Math.ceil(location.getX()) - 0.5, Math.ceil(location.getY()) - 0.5, Math.ceil(location.getZ()) - 0.5, yaw, 0);
    }

    private void checkForStart() {
        if(players.size() >= startLimit) {
            if(isInitialized()) startTimer();
        } else {
            stopTimer();
        }
    }

    private void clearPlayer(Player player) {
        player.getInventory().clear();
        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    private void startTimer() {

        if(startingTask == null) startingTask = Bukkit.getScheduler().runTaskTimer(BlackBedWars.instance, new Runnable() {

            @Override
            public void run() {

                if(startingTimer == 0) {
                    startingTimer = 30;
                    startGame();
                    startingTask.cancel();
                }

                startingTimer--;
                
            }
            
        }, 20, 20);

    }

    private void stopTimer() {
        if(startingTask != null) {
            startingTask.cancel();
            startingTask = null;
            startingTimer = 30;
        }
    }

}