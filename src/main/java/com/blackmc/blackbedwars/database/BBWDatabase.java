package com.blackmc.blackbedwars.database;

import java.io.Serializable;
import java.util.UUID;

import com.blackmc.blackbedwars.core.FriendList;
import com.blackmc.blackbedwars.core.Party;
import com.blackmc.blackbedwars.core.PartyMember;

import org.bukkit.entity.Player;

public class BBWDatabase {

    private static BBWDatabase instance;

    private Database database;

    private BBWDatabase(Database database) {
        this.database = database;
    }
    
    public static BBWDatabase getDatabase() {
        if(instance == null) instance = new BBWDatabase(DatabaseManager.getDatabase());
        return instance;
    }

    public boolean friendListRegistered(Player player) {
        return database.contains(Party.class, player.getUniqueId());
    }

    public FriendList getOrCreateFriendList(Player player) {
        if(!friendListRegistered(player)) database.save(new FriendList(player));
        return database.get(FriendList.class, player.getUniqueId());
    }

    public boolean partyRegistered(Player player) {
        return partyRegistered(player.getUniqueId());
    }

    public boolean partyRegistered(UUID uuid) {
        return partyMemberRegistered(uuid) ? getPartyMember(uuid).getParty() != null : false;
    }

    public Party getParty(Player player) {
        return getParty(player.getUniqueId());
    }
    
    public Party getParty(UUID uuid) {
        return partyMemberRegistered(uuid) ? getPartyMember(uuid).getParty() : null;
    }

    public Party getOrCreateParty(Player player) {
        return getOrCreateParty(player.getUniqueId());
    }

    public Party getOrCreateParty(UUID uuid) {
        PartyMember member = partyMemberRegistered(uuid) ? getPartyMember(uuid) : new PartyMember(uuid);
        Party party = partyRegistered(uuid) ? getParty(uuid) : new Party(uuid);
        member.setParty(party);
        save(party);
        save(member);
        return party;
    }

    public void removeParty(Player player) {
        database.delete(player.getUniqueId());
    }

    public boolean partyMemberRegistered(Player player) {
        return partyMemberRegistered(player.getUniqueId());
    }

    public boolean partyMemberRegistered(UUID uuid) {
        return database.contains(PartyMember.class, uuid);
    }

    public PartyMember getPartyMember(Player player) {
        return getPartyMember(player.getUniqueId());
    }

    public PartyMember getPartyMember(UUID uuid) {
        return database.get(PartyMember.class, uuid);
    }

    public PartyMember getOrCreatePartyMember(Player player) {
        return getOrCreatePartyMember(player.getUniqueId());
    }

    public PartyMember getOrCreatePartyMember(UUID uuid) {
        return partyMemberRegistered(uuid) ? getPartyMember(uuid) : new PartyMember(uuid);
    }

    public <T> T get(Class<T> type, Serializable id) {
        return database.get(type, id);
    }

    public void save(Object object) {
        database.save(object);
    }
    
}