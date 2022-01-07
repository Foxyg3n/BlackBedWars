package com.blackmc.blackbedwars.events;

import com.blackmc.blackbedwars.core.Game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Game game;

    public GameStartEvent(Game game) {
        this.game = game;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Game getGame() {
        return this.game;
    }
    
}