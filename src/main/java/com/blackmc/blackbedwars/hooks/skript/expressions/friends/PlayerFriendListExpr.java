package com.blackmc.blackbedwars.hooks.skript.expressions.friends;


import java.util.ArrayList;
import java.util.UUID;

import com.blackmc.blackbedwars.core.FriendList;
import com.blackmc.blackbedwars.database.BBWDatabase;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;

public class PlayerFriendListExpr extends SimpleExpression<Player> {

    static {
        Skript.registerExpression(PlayerFriendListExpr.class, Player.class, ExpressionType.SIMPLE, "(friends|friendlist) of %player%", "%player%['s] (friends|friendlist)");
    }
    Expression<Player> playerExpression;

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
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player friend list with expression player " + playerExpression.toString(event, debug);
    }

    @Override
    @Nullable
    protected Player[] get(Event event) {
        BBWDatabase database = BBWDatabase.getDatabase();
        Player player = playerExpression.getSingle(event);
        ArrayList<UUID> friends = database.getOrCreateFriendList(player).getFriends();
        if(friends != null && !friends.isEmpty()) return new ArrayList<Player>() {{
            for(UUID uuid : friends) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) add(player);
            }
        }}.toArray(new Player[0]);
        return null;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) {
            return CollectionUtils.array(Player.class);
        }
        return null;
    }


    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        BBWDatabase database = BBWDatabase.getDatabase();
        Player player = playerExpression.getSingle(event);
        Player friend = (Player) delta[0];

        if (player == null || friend == null) return;
        
        FriendList friendList = database.getOrCreateFriendList(player);
        switch (mode) {
            case ADD:
                friendList.addFriend(friend.getUniqueId());
                break;
            case REMOVE:
                friendList.removeFriend(friend.getUniqueId());
                break;
            default:
        }
    }
    
}