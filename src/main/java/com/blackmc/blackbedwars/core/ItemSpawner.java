package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.blackmc.blackbedwars.BlackBedWars;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.md_5.bungee.api.ChatColor;

public class ItemSpawner {

    ArrayList<Spawner> spawners = new ArrayList<Spawner>();
    ArmorStand armorStand;
    boolean hasTitle;
    String title;
    ItemSpawnerType type;

    private static ItemSpawner getItemSpawner(Location location, Spawner... spawners) {
        return getItemSpawner(location, null, spawners);
    }

    private static ItemSpawner getItemSpawner(Location location, String title, Spawner... spawners) {
        location.setX(Math.ceil(location.getX()));
        location.setZ(Math.ceil(location.getZ()));
        location.add(-0.5, 3, -0.5);

        Iterator<Entity> entity = location.getWorld().getNearbyEntities(location, 0, 0, 0).iterator();
        ArmorStand armorStand = (ArmorStand) (entity.hasNext() ? entity.next() : location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND));

        armorStand.setVisible(false);
        armorStand.setPersistent(true);
        armorStand.setAI(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setMarker(true);
        if(title != null) {
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(title);
        }

        ItemSpawner itemSpawner = new ItemSpawner();
        itemSpawner.armorStand = armorStand;
        itemSpawner.spawners.addAll(Arrays.asList(spawners));
        itemSpawner.hasTitle = title != null;
        itemSpawner.title = title;
        itemSpawner.type = null;

        return itemSpawner;
    }

    public static ItemSpawner getBasicItemSpawner(Location location) {
        ItemSpawner itemSpawner = getItemSpawner(location, new Spawner(SpawnerType.IRON), new Spawner(SpawnerType.GOLD));
        itemSpawner.type = ItemSpawnerType.BASIC;
        return itemSpawner;
    }

    public static ItemSpawner getDiamondItemSpawner(Location location) {
        ItemSpawner itemSpawner = getItemSpawner(location, ChatColor.AQUA + "Diament % pojawi się za" + ChatColor.AQUA + " & " + ChatColor.AQUA + "sekund", new Spawner(SpawnerType.DIAMOND));
        itemSpawner.type = ItemSpawnerType.DIAMOND;
        return itemSpawner;
    }

    public static ItemSpawner getEmeraldItemSpawner(Location location) {
        ItemSpawner itemSpawner = getItemSpawner(location, ChatColor.GREEN + "Emerald % pojawi się za" + ChatColor.GREEN + " & " + ChatColor.GREEN + "sekund",  new Spawner(SpawnerType.EMERALD));
        itemSpawner.type = ItemSpawnerType.EMERALD;
        return itemSpawner;
    }

    public static ItemSpawner getItemSpawnerFromType(Location location, ItemSpawnerType type) {
        switch(type) {
            case BASIC:
                return getBasicItemSpawner(location);
            case DIAMOND:
                return getDiamondItemSpawner(location);
            case EMERALD:
                return getEmeraldItemSpawner(location);
            default:
                return null;
        }
    }

    public ItemSpawnerType getType() {
        return type;
    }

    public Location getLocation() {
        return armorStand.getLocation();
    }

    public void reset() {
        for(Spawner spawner : spawners) {
            spawner.reset();
        }
    }

    public void upgrade() {
        for(Spawner spawner : spawners) {
            spawner.upgrade();
        }
    }

    public void startSpawner(Game game) {

        for(Spawner spawner : spawners) {

            spawner.task = Bukkit.getScheduler().runTaskTimer(BlackBedWars.instance, new Runnable() {

                int counter = 0;
                double timer = spawner.timer;
                Location dropLocation = armorStand.getLocation().subtract(0, 3, 0);

                @Override
                public void run() {

                    if(game.isStopped()) spawner.task.cancel();

                    if(timer <= 0.0) {

                        armorStand.getWorld().dropItem(dropLocation, spawner.item);
                        timer = spawner.timer;

                    }

                    if(counter == 120) {
                        upgrade();
                    } else if(counter == 300) {
                        upgrade();
                    }

                    if(hasTitle) armorStand.setCustomName(title.replace("%", StringUtils.repeat("I", spawner.level)).replace("&", String.valueOf((int) timer)));

                    timer--;
                    counter++;
                    
                }

            }, 20, 20);

        }

    }

    public void startFurnace(Game game) {

        for(Spawner spawner : spawners) {

            spawner.task = Bukkit.getScheduler().runTaskTimer(BlackBedWars.instance, new Runnable() {

                double timer = spawner.timer;
                Location dropLocation = armorStand.getLocation().subtract(0, 3, 0);

                @Override
                public void run() {

                    if(game.isStopped()) spawner.task.cancel();

                    if(timer <= 0.0) {
                        
                        armorStand.getWorld().dropItem(dropLocation, spawner.item);
                        timer = spawner.timer;

                    }

                    timer -= 0.2;
                    
                }

            }, 4, 4);

        }

    }

    public void stopSpawning() {

        for(final Spawner spawner : spawners) {

            if(spawner.task != null) {
                spawner.task.cancel();
                spawner.task = null;
            }

        }

    }
    
}