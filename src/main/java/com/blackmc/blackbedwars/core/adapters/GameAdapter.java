package com.blackmc.blackbedwars.core.adapters;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameType;
import com.blackmc.blackbedwars.core.ItemSpawner;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.core.TeamType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import org.bukkit.Location;

import ch.njol.util.Pair;

public class GameAdapter implements JsonDeserializer<Game>, JsonSerializer<Game> {

    MVWorldManager worldManager = BlackBedWars.mvCore.getMVWorldManager();

    @Override
    public Game deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String name = obj.get("name").getAsString();
        GameType gameType = GameType.valueOf(obj.get("type").getAsString());
        boolean shouldInit = obj.get("shouldInit").getAsBoolean();

        ArrayList<ItemSpawner> itemSpawners = context.deserialize(obj.get("itemSpawners").getAsJsonArray(), new TypeToken<ArrayList<ItemSpawner>>() {}.getType());
        Location spectatorSpawn = context.deserialize(obj.get("spectatorSpawn"), new TypeToken<Location>() {}.getType());

        Game game = new Game(name, worldManager.getMVWorld(name), gameType);
        
        for(TeamType teamType : TeamType.values()) {
            if(obj.has("spawn-" + teamType.name().toLowerCase())) {
                game.getTeam(teamType).setSpawn(context.deserialize(obj.get("spawn-" + teamType.name().toLowerCase()), new TypeToken<Location>() {}.getType()));
            }
            if(obj.has("bed1-" + teamType.name().toLowerCase()) && obj.has("bed2-" + teamType.name().toLowerCase())) {
                Pair<Location, Location> bed = new Pair<Location, Location>(context.deserialize(obj.get("bed1-" + teamType.name().toLowerCase()), new TypeToken<Location>() {}.getType()), context.deserialize(obj.get("bed2-" + teamType.name().toLowerCase()), new TypeToken<Location>() {}.getType()));
                game.getTeam(teamType).setBed(bed);
                game.getBeds().put(bed, teamType);
            }
            if(obj.has("furnace-" + teamType.name().toLowerCase())) {
                game.getTeam(teamType).setFurnace(context.deserialize(obj.get("furnace-" + teamType.name().toLowerCase()), new TypeToken<ItemSpawner>() {}.getType()));
            }
        }

        game.setItemSpawners(itemSpawners);
        game.setSpectatorSpawn(spectatorSpawn);

        game.shouldInit(shouldInit);

        return game;

    }

    @Override
    public JsonElement serialize(Game game, Type type, JsonSerializationContext context) {
        
        JsonObject obj = new JsonObject();

        obj.addProperty("name", game.getName());
        obj.addProperty("type", game.getType().toString().toUpperCase());
        obj.addProperty("shouldInit", game.isWaiting() ? true : false);
        obj.add("itemSpawners", context.serialize(game.getItemSpawners()));
        obj.add("spectatorSpawn", context.serialize(game.getSpectatorSpawn()));
        for(Team team : game.teams.getTeams()) {
            obj.add("spawn-" + team.getType().name().toLowerCase(), context.serialize(team.getSpawn()));
            obj.add("bed1-" + team.getType().name().toLowerCase(), context.serialize(team.getBed().getKey()));
            obj.add("bed2-" + team.getType().name().toLowerCase(), context.serialize(team.getBed().getValue()));
            obj.add("furnace-" + team.getType().name().toLowerCase(), context.serialize(team.getFurnace()));
        }

        return obj;
    }
    
}
