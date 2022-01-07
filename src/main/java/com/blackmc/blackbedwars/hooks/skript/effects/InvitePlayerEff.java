package com.blackmc.blackbedwars.hooks.skript.effects;

import com.blackmc.blackbedwars.core.Invitable;
import com.blackmc.blackbedwars.database.BBWDatabase;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class InvitePlayerEff extends Effect {
    
    static {
        Skript.registerEffect(InvitePlayerEff.class, "invite %player% to [the] %player%['s] (0¦friend(s|list)|1¦party)");
    }
    Expression<Player> recipentExpression;
    Expression<Player> inviterExpression;
    int option;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        recipentExpression = (Expression<Player>) exprs[0];
        inviterExpression = (Expression<Player>) exprs[1];
        option = matchedPattern;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Invite player expression with recipentExpression: " + recipentExpression.toString() + " and inviterExpression: " + inviterExpression.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        BBWDatabase database = BBWDatabase.getDatabase();
        Player inviter = inviterExpression.getSingle(event);
        Player recipent = recipentExpression.getSingle(event);
        Invitable invitable = option == 0 ? database.getOrCreateFriendList(inviter) : database.getOrCreateParty(inviter);
        if(invitable != null) invitable.invite(recipent);
    }
    
}