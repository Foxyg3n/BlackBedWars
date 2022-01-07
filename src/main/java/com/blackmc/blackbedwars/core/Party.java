package com.blackmc.blackbedwars.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.blackmc.blackbedwars.database.BBWDatabase;
import com.blackmc.blackbedwars.database.DatabaseManager;

import org.bukkit.entity.Player;

@Entity
@Table(name = "party")
public class Party extends Invitable implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "party_id")
    private long partyId;

    @Column(name = "owner", columnDefinition = "BINARY(16)")
    private UUID owner;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "party")
    private List<PartyMember> members = new ArrayList<PartyMember>();
    
    @Column(name = "isPrivate")
    private boolean isPrivate = true;

    protected Party() {};

    public Party(Player owner, boolean isPrivate) {
        this(owner);
        this.isPrivate = isPrivate;
    }

    public Party(UUID uuid, boolean isPrivate) {
        this(uuid);
        this.isPrivate = isPrivate;
    }
 
    public Party(Player owner) {
        this(owner.getUniqueId());
    }

    public Party(UUID uuid) {
        this.owner = uuid;
    }

    @Override
    public void invite(Player player) {
        super.invite(player);
        BBWDatabase.getDatabase().getOrCreatePartyMember(player).addInvitedParty(this);
    }

    @Override
    public void acceptMember(Player player) {
        super.acceptMember(player);
        if(isInvited(player)) BBWDatabase.getDatabase().getPartyMember(player).removeInvitedParty(this);
    }

    @Override
    public void rejectMember(Player player) {
        super.rejectMember(player);
        if(isInvited(player)) BBWDatabase.getDatabase().getPartyMember(player).removeInvitedParty(this);
    }

    @Override
    protected void acceptInvite(Player player) {
        addMember(player);
        save();
        BBWDatabase.getDatabase().getPartyMember(player).save();
    }

    public UUID getOwner() {
        return owner;
        // Player player = Bukkit.getPlayer(owner);
        // if(player == null) return Bukkit.getOfflinePlayer(owner);
        // return player;
    }

    public void setOwner(Player player) {
        this.owner = player.getUniqueId();
    }

    public List<UUID> getMembers() {
        return members.stream().map(member -> member.getUUID()).collect(Collectors.toList());
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isMember(Player player) {
        BBWDatabase database = BBWDatabase.getDatabase();
        return database.partyMemberRegistered(player) ? members.contains(database.getPartyMember(player)) : false;
    }

    public void addMember(Player player) {
        BBWDatabase database = BBWDatabase.getDatabase();
        PartyMember member = database.getOrCreatePartyMember(player);
        member.setParty(this);
        members.add(member);
    }

    public void removeMember(Player player) {
        BBWDatabase database =  BBWDatabase.getDatabase();
        if(isMember(player)) {
            PartyMember member = database.getPartyMember(player);
            members.remove(member);
            member.setParty(null);
        }
    }

    public void save() {
        DatabaseManager.getDatabase().save(this);
    }
 
}