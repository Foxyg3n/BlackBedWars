package com.blackmc.blackbedwars.hooks.skript.events;

import com.blackmc.blackbedwars.events.GameEndEvent;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;

public class GameEndEvt extends SkriptEvent {

    static {
        Skript.registerEvent("Game End", GameEndEvt.class, GameEndEvent.class, "game end");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Game End event";
    }

    @Override
    public boolean check(Event event) {
        return true;
    }
    
}