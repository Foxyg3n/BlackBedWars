package com.blackmc.blackbedwars.hooks.skript.conditions;


import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class PlayerHasTeam extends Condition {

    static {
        Skript.registerCondition(PlayerHasTeam.class, "[the] %player% has team");
    }

    Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player has team " + playerExpression.toString(event, debug);
    }

    @Override
    public boolean check(Event event) {
        Player player = playerExpression.getSingle(event);
        Game game = GameManager.getGame(player.getWorld().getName());
        if(game != null) {
            return game.getTeam(player) != null;
        }
        return false;
    }
    
}