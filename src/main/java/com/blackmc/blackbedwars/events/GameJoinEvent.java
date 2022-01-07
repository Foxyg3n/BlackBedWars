package com.blackmc.blackbedwars.events;

import com.blackmc.blackbedwars.core.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameJoinEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private Game game;

    public GameJoinEvent(Player player, Game game) {
        this.player = player;
        this.game = game;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Game getGame() {
        return game;
    }
    
}