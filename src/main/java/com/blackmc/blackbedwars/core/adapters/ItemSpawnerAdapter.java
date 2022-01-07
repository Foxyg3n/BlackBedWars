package com.blackmc.blackbedwars.core.adapters;

import java.lang.reflect.Type;

import com.blackmc.blackbedwars.core.ItemSpawner;
import com.blackmc.blackbedwars.core.ItemSpawnerType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.bukkit.Location;

public class ItemSpawnerAdapter implements JsonDeserializer<ItemSpawner>, JsonSerializer<ItemSpawner> {

    @Override
    public ItemSpawner deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        ItemSpawnerType spawnerType = ItemSpawnerType.valueOf(obj.get("type").getAsString());
        Location location = context.deserialize(obj.get("location"), new TypeToken<Location>() {}.getType());

        return ItemSpawner.getItemSpawnerFromType(location.subtract(0, 3, 0), spawnerType);
    }

    @Override
    public JsonElement serialize(ItemSpawner itemSpawner, Type type, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();

        obj.addProperty("type", itemSpawner.getType().toString().toUpperCase());
        obj.add("location", context.serialize(itemSpawner.getLocation()));

        return obj;
    }
    
}
