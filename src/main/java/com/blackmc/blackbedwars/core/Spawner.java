package com.blackmc.blackbedwars.core;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class Spawner {

    ItemStack item;
    SpawnerType type;
    double timer;
    int level;
    double level0;
    double level1;
    double level2;
    double level3;
    double level4;
    BukkitTask task;
 
    public Spawner(SpawnerType type) {
        switch(type) {
            case IRON:
                this.item = new ItemStack(Material.IRON_INGOT);
                this.level0 = 1;
                this.level1 = 0.8;
                this.level2 = 0.6;
                this.level3 = 0.4;
                this.level4 = 0.2;
                this.type = type;
                break;
            case GOLD:
                this.item = new ItemStack(Material.GOLD_INGOT);
                this.level0 = 8;
                this.level1 = 6;
                this.level2 = 4;
                this.level3 = 3;
                this.level4 = 2;
                this.type = type;
                break;
            case DIAMOND:
                this.item = new ItemStack(Material.DIAMOND);
                this.level0 = 30;
                this.level1 = 15;
                this.level2 = 7;
                this.level3 = this.level2;
                this.level4 = this.level2;
                this.type = type;
                break;
            case EMERALD:
                this.item = new ItemStack(Material.EMERALD);
                this.level0 = 60;
                this.level1 = 30;
                this.level2 = 16;
                this.level3 = this.level2;
                this.level4 = this.level2;
                this.type = type;
                break;
        }

        this.timer = this.level0;
        this.level = 0;
        this.task = null;

    }

    public void reset() {
        this.timer = this.level0;
        this.level = 0;
    }

    public void upgrade() {
        if(this.level == 4) return;
        level++;
        this.timer = (this.level == 1 ? this.level1 : (this.level == 2 ? this.level2 : (this.level == 3 ? this.level3 : this.level4)));
    }

}