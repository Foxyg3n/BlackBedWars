package com.blackmc.blackbedwars.hooks.skript.conditions;


import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.Team;
import com.blackmc.blackbedwars.core.TeamType;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class PlayerIsInTeam extends Condition {

    static {
        Skript.registerCondition(PlayerIsInTeam.class,
        "[the] %player%('s| is) in team red",
        "[the] %player%('s| is) in team green",
        "[the] %player%('s| is) in team blue",
        "[the] %player%('s| is) in team yellow",
        "[the] %player%('s| is) in team pink",
        "[the] %player%('s| is) in team white",
        "[the] %player%('s| is) in team gray",
        "[the] %player%('s| is) in team aqua");
    }

    Expression<Player> playerExpression;
    TeamType type;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        switch(matchedPattern) {
            case 0:
                type = TeamType.RED;
                break;
            case 1:
                type = TeamType.GREEN;
                break;
            case 2:
                type = TeamType.BLUE;
                break;
            case 3:
                type = TeamType.YELLOW;
                break;
            case 4:
                type = TeamType.PINK;
                break;
            case 5:
                type = TeamType.WHITE;
                break;
            case 6:
                type = TeamType.GRAY;
                break;
            case 7:
                type = TeamType.AQUA;
                break;
            default:
                type = null;
                break;
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player in team " + playerExpression.toString(event, debug);
    }

    @Override
    public boolean check(Event event) {
        Player player = playerExpression.getSingle(event);
        Game game = GameManager.getGame(player.getWorld().getName());
        if(game != null && type != null) {
            Team team = game.getTeam(player);
            if(team != null) {
                return team.getType().equals(type);
            }
        }
        return false;
    }
    
}