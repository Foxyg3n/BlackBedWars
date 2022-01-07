package com.blackmc.blackbedwars.core;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class Invitable {
 
    protected static HashMap<UUID, Invitable> invitations = new HashMap<UUID, Invitable>();

    public void invite(Player player) {
        if(!isInvited(player)) invitations.put(player.getUniqueId(), this);
    }

    public boolean isInvited(Player player) {
        return invitations.containsKey(player.getUniqueId());
    }

    public void acceptMember(Player player) {
        if(isInvited(player)) acceptInvite(player);
        deleteInvite(player);
    }

    // FIXME: This is probably useless due to delete() func working the same way
    public void rejectMember(Player player) {
        if(isInvited(player)) deleteInvite(player);
    }

    protected void deleteInvite(Player player) {
        invitations.remove(player.getUniqueId());
    }

    protected abstract void acceptInvite(Player player);

}