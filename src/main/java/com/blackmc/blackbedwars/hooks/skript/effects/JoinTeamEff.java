package com.blackmc.blackbedwars.hooks.skript.effects;


import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.TeamType;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class JoinTeamEff extends Effect {

    static {
        Skript.registerEffect(JoinTeamEff.class, "add %player% to team %string%");
    }

    Expression<Player> playerExpression;
    Expression<String> typeExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        typeExpression = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Join player to a team with expression player " + playerExpression.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Player player = playerExpression.getSingle(event);
        Game game = GameManager.getGame(player.getWorld().getName());
        TeamType teamType = TeamType.getByName(typeExpression.getSingle(event));
        if(game != null && teamType != null) game.addTeamMember(teamType, player);
    }
    
}
