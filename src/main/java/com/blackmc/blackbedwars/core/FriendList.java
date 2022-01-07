package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackmc.blackbedwars.database.DatabaseManager;

import org.bukkit.entity.Player;

@Entity
@Table(name = "friend_list")
public class FriendList extends Invitable {
    
    @Id
    @Column(name = "owner", columnDefinition = "BINARY(16)")
    private UUID owner;
    
    @Column(name = "favourite_friends")
    private ArrayList<UUID> favFriends = new ArrayList<UUID>();

    @Column(name = "friends")
    private ArrayList<UUID> friends = new ArrayList<UUID>();

    protected FriendList() {};

    public FriendList(Player player) {
        this.owner = player.getUniqueId();
        save();
    }

    @Override
    protected void acceptInvite(Player player) {
        if(isInvited(player)) addFriend(player.getUniqueId());
    }

    public UUID getOwner() {
        return owner;
    }

    public ArrayList<UUID> getFriends() {
        return friends;
    }

    public ArrayList<UUID> getFavouriteFriends() {
        return favFriends;
    }

    public boolean isFriend(UUID uuid) {
        return friends.contains(uuid);
    }

    public boolean isFavouriteFriend(UUID uuid) {
        return favFriends.contains(uuid);
    }

    public void addFriend(UUID uuid) {
        friends.add(uuid);
        save();
    }

    public void removeFriend(UUID uuid) {
        if(isFavouriteFriend(uuid)) removeFavouriteFriend(uuid);
        friends.remove(uuid);
        save();
    }

    public void addFavouriteFriend(UUID uuid) {
        if(!isFriend(uuid)) return;
        favFriends.add(uuid);
        save();
    }

    public void removeFavouriteFriend(UUID uuid) {
        favFriends.remove(uuid);
        save();
    }

    public void save() {
        DatabaseManager.getDatabase().save(this);
    }

}
