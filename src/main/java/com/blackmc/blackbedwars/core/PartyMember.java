package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.blackmc.blackbedwars.database.BBWDatabase;
import com.blackmc.blackbedwars.database.DatabaseManager;

import org.bukkit.entity.Player;

@Entity
@Table(name = "party_member")
public class PartyMember {

    @Id
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Column(name = "invited_parties")
    private ArrayList<UUID> invitedParties = new ArrayList<>();

    protected PartyMember() {};
    
    public PartyMember(Player player) {
        this(player.getUniqueId());
    }

    public PartyMember(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void addInvitedParty(Party party) {
        invitedParties.add(party.getOwner());
        save();
    }

    public void removeInvitedParty(Party party) {
        invitedParties.remove(party.getOwner());
        save();
    }

    public Party getInvitedParty() {
        return invitedParties.isEmpty() ? null : BBWDatabase.getDatabase().getParty(invitedParties.get(0));
    }

    public void save() {
        DatabaseManager.getDatabase().save(this);
    }

}