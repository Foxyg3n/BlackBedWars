package com.blackmc.blackbedwars.commands;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Party;
import com.blackmc.blackbedwars.database.BBWDatabase;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCmd implements CommandExecutor {

    public PartyCmd(BlackBedWars plugin) {
        plugin.getCommand("party").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        BBWDatabase database = BBWDatabase.getDatabase();

        if(sender instanceof Player) {
            
            Player player = (Player) sender;

            if(args.length == 0) {

                Messanger.sendMessage(player, "---------- Komendy Party ----------");
                // Party Help
                Messanger.sendMessage(player, "-----------------------------------");

            } else if(args.length == 1) {
            
                if(args[0].equals("stworz")) {

                    database.getOrCreateParty(player);

                } else if(args[0].equals("zapros")) {

                    Messanger.sendWarning(player, "Podaj nick gracza, którego chcesz zaprosić");

                } else if(args[0].equals("przyjmi") || args[0].equals("akceptuj")) {

                    System.out.println(database.getPartyMember(player).getUUID());
                    Party party = database.getPartyMember(player).getInvitedParty();
                    if(party != null) {
                        party.acceptMember(player);
                        Messanger.sendConfirm(player, "Dołączyłeś do party");
                    } else {
                        Messanger.sendWarning(player, "Nie posiadasz oczekujących zaproszeń");
                    }
                    
                } else if(args[0].equals("opusc")) {

                    Party party = database.getPartyMember(player).getInvitedParty();
                    if(party != null) party.rejectMember(player);

                } else if(args[0].equals("rozwiaz")) {

                    database.removeParty(player);

                } else if(args[0].equals("wyrzuc")) {

                } else if(args[0].equals("ustawienia")) {

                }

            } else if(args.length == 2) {
        
                if(args[0].equals("zapros")) {

                    Player recipent = Bukkit.getPlayer(args[1]);
                    if(recipent != null) {
                        Party party = database.getOrCreateParty(player);
                        party.invite(recipent);
                        Messanger.sendConfirm(player, "Wysłano zaprosznie do party graczowi: " + recipent.getName());
                        Messanger.sendMessage(recipent, player.getName() + " zaprosił Cię do party");
                    } else {
                        Messanger.sendWarning(player, "Nie znaleziono gracza o podanym nicku");
                    }
                    
                } else if(args[0].equals("wyrzuc")) {

                    Player kickedPlayer = Bukkit.getPlayer(args[1]);
                    if(kickedPlayer != null) {
                        database.getOrCreateParty(player).removeMember(kickedPlayer);
                        Messanger.sendConfirm(player, "Wyrzuono gracza z party: " + kickedPlayer.getName());
                        Messanger.sendMessage(kickedPlayer, "Zostałeś wyrzucony z party");
                    }
                } else {
                    Messanger.sendWarning(player, "Nie znaleziono gracza o podanym nicku");
                }

            }

        }

        return true;
    }
    
}
