package com.blackmc.blackbedwars.hooks.skript.events;

import com.blackmc.blackbedwars.events.GameJoinEvent;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;

public class JoinGameEvt extends SkriptEvent {

    static {
        Skript.registerEvent("Player Game Join", JoinGameEvt.class, GameJoinEvent.class, "game join");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player Game Join event";
    }

    @Override
    public boolean check(Event event) {
        return true;
    }
    
}
