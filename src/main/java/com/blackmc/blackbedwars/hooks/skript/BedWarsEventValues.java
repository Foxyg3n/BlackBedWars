package com.blackmc.blackbedwars.hooks.skript;

import com.blackmc.blackbedwars.events.GameEndEvent;
import com.blackmc.blackbedwars.events.GameStartEvent;
import com.blackmc.blackbedwars.events.GameJoinEvent;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

public final class BedWarsEventValues {
    
    static {

        EventValues.registerEventValue(GameStartEvent.class, String.class, new Getter<String, GameStartEvent>() {
            @Override
            @Nullable
            public String get(GameStartEvent e) {
                return e.getGame().getName();
            }
        }, 0);
    
        EventValues.registerEventValue(GameEndEvent.class, String.class, new Getter<String, GameEndEvent>() {
            @Override
            @Nullable
            public String get(GameEndEvent e) {
                return e.getGame().getName();
            }
        }, 0);
    
        EventValues.registerEventValue(GameJoinEvent.class, Player.class, new Getter<Player, GameJoinEvent>() {
            @Override
            @Nullable
            public Player get(GameJoinEvent e) {
                return e.getPlayer();
            }
        }, 0);

    }

}
