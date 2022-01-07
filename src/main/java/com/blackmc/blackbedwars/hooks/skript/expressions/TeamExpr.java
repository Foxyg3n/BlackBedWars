package com.blackmc.blackbedwars.hooks.skript.expressions;

import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.core.TeamType;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class TeamExpr extends SimpleExpression<Team> {

    static {
        Skript.registerExpression(TeamExpr.class, Team.class, ExpressionType.SIMPLE,
        "team red of game %string%",
        "team green of game %string%",
        "team blue of game %string%",
        "team yellow of game %string%",
        "team pink of game %string%",
        "team white of game %string%",
        "team gray of game %string%",
        "team aqua of game %string%");
    }
    Expression<String> gameExpression;
    TeamType type;

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
        gameExpression = (Expression<String>) exprs[0];
        type = TeamType.values()[matchedPattern];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Team with expression game " + gameExpression.toString(event, debug);
    }

    @Override
    @Nullable
    protected Team[] get(Event event) {
        String name = gameExpression.getSingle(event);
        Game game = GameManager.getGame(name);
        if(game != null) return new Team[] { game.getTeam(type) };
        return null;
    }
    
}
