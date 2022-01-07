package com.blackmc.blackbedwars.hooks.skript.effects;


import com.blackmc.blackbedwars.core.Team;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class UpgradeFurnaceEff extends Effect {

    static {
        Skript.registerEffect(UpgradeFurnaceEff.class, "upgrade furnace of %team%");
    }

    Expression<Team> teamExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        teamExpression = (Expression<Team>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Upgrade furnace for a team " + teamExpression.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Team team = teamExpression.getSingle(event);
        team.getFurnace().upgrade();
    }
    
}
