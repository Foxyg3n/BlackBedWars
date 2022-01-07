package com.blackmc.blackbedwars.hooks.skript.events;

import com.blackmc.blackbedwars.events.GameStartEvent;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;

public class GameStartEvt extends SkriptEvent {

    static {
        Skript.registerEvent("Game Start", GameStartEvt.class, GameStartEvent.class, "game start");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Game Start event";
    }

    @Override
    public boolean check(Event event) {
        return true;
    }
    
}
