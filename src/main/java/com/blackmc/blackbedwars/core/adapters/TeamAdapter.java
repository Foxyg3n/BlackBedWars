package com.blackmc.blackbedwars.core.adapters;

import java.lang.reflect.Type;

import com.blackmc.blackbedwars.core.Team;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TeamAdapter implements JsonDeserializer<Team>, JsonSerializer<Team> {

    @Override
    public Team deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(Team team, Type type, JsonSerializationContext context) {
        
        JsonObject obj = new JsonObject();
        
        obj.add("loc1",  context.serialize(team.getBed().getKey()));
        obj.add("loc2",  context.serialize(team.getBed().getValue()));
        obj.add("spawn",  context.serialize(team.getSpawn()));

        return null;
    }
    
}