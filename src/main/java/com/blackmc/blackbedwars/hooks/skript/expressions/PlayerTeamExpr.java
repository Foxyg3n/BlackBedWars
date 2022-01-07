package com.blackmc.blackbedwars.hooks.skript.expressions;


import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class PlayerTeamExpr extends SimpleExpression<Team> {

    static {
        Skript.registerExpression(PlayerTeamExpr.class, Team.class, ExpressionType.SIMPLE, "[the] team of %player%", "[the] %player%['s] team");
    }
    Expression<Player> playerExpression;

    @Override
    public Class<? extends Team> getReturnType() {
        return Team.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        playerExpression = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player team with expression player " + playerExpression.toString(event, debug);
    }

    @Override
    @Nullable
    protected Team[] get(Event event) {
        Player player = playerExpression.getSingle(event);
        Game game = GameManager.getGame(player.getWorld().getName());
        if(game != null) return new Team[] { game.getTeam(player) };
        return null;
    }
    
}