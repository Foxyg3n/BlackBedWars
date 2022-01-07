package com.blackmc.blackbedwars.hooks.skript.effects;

import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class LeaveGameEff extends Effect {

    static {
        Skript.registerEffect(JoinTeamEff.class, "remove %player% from game");
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
        return "Leave player from game with expression player " + playerExpression.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Player player = playerExpression.getSingle(event);
        Game game = GameManager.getGame(player.getWorld().getName());
        if(game != null) game.removePlayer(player);
    }
    
}
