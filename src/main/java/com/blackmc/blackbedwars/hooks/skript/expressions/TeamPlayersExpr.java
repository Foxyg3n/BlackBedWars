package com.blackmc.blackbedwars.hooks.skript.expressions;

import java.util.ArrayList;
import java.util.UUID;

import com.blackmc.blackbedwars.core.Team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class TeamPlayersExpr extends SimpleExpression<Player> {

    static {
        Skript.registerExpression(TeamPlayersExpr.class, Player.class, ExpressionType.SIMPLE, "[the] players of %team%", "[the] %team%['s] players");
    }
    Expression<Team> teamExpression;

    @Override
    public Class<? extends Player> getReturnType() {
        return Player.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        teamExpression = (Expression<Team>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player team with expression player " + teamExpression.toString(event, debug);
    }

    @Override
    @Nullable
    protected Player[] get(Event event) {
        Team team = teamExpression.getSingle(event);
        return new ArrayList<Player>() {{
            for(UUID uuid : team.getMembers()) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) add(player);
            }
        }}.toArray(new Player[0]);
    }
    
}