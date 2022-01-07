package com.blackmc.blackbedwars.commands;

import java.util.ArrayList;
import java.util.List;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.ItemSpawner;
import com.blackmc.blackbedwars.core.ItemSpawnerType;
import com.blackmc.blackbedwars.core.TeamType;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ItemSpawnerCmd implements CommandExecutor, TabCompleter {
    
    public ItemSpawnerCmd(BlackBedWars plugin) {
        plugin.getCommand("itemspawner").setExecutor(this);
        plugin.getCommand("itemspawner").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(args.length == 0) {
    
                Messanger.sendWarning(player, "Wprowadź więcej argumentów");
    
            } else if(args.length == 1) {

                if(args[0].equals("remove")) {

                    Game game = GameManager.getGame(player.getWorld());

                    if(game != null) {

                        if(!game.getItemSpawners().isEmpty()) {

                            double comparator = player.getLocation().distance(game.getItemSpawners().get(0).getLocation());
                            int index = 0;
    
                            for(ItemSpawner itemSpawner : game.getItemSpawners()) {
                                double distance = player.getLocation().distance(itemSpawner.getLocation());
                                if(comparator > distance) {
                                    comparator = distance;
                                    index = game.getItemSpawners().indexOf(itemSpawner);
                                }
                            }

                            game.removeItemSpawner(game.getItemSpawners().get(index));

                        } else {
    
                            Messanger.sendWarning(player, "Nie znaleziono żadnych spawnerów w rozgrywce");

                        }

                    } else {
    
                        Messanger.sendWarning(player, "Nie znaleziono gry");

                    }

                } else {
    
                    Messanger.sendWarning(player, "Wprowadziłeś złe argumenty");

                }

            } else if(args.length == 2) {

                if(args[0].equals("create")) {

                    String typeName = args[1];
                    ItemSpawnerType itemSpawnerType = ItemSpawnerType.getByName(typeName);

                    if(itemSpawnerType == null) {
                        Messanger.sendWarning(player, "Niepoprawny typ itemspawnera");
                        return true;
                    }

                    if(itemSpawnerType.equals(ItemSpawnerType.BASIC)) {
                        Messanger.sendWarning(player, "Niepoprawny typ itemspawnera");
                        return true;
                    }

                    if(GameManager.presentGame(player.getWorld())) {

                        ItemSpawner itemSpawner;

                        switch(itemSpawnerType) {
                            case DIAMOND:
                                itemSpawner = ItemSpawner.getDiamondItemSpawner(player.getLocation());
                                break;
                            case EMERALD:
                                itemSpawner = ItemSpawner.getEmeraldItemSpawner(player.getLocation());
                                break;
                            default:
                                itemSpawner = null;
                                break;
                        }

                        GameManager.getGame(player.getWorld()).addItemSpawner(itemSpawner);

                        Messanger.sendConfirm(player, "Utworzono nowy itemspawner");

                    } else {

                        Messanger.sendWarning(player, "Nie udało się utworzyć itemspawnera");

                    }

                }

            } else if(args.length == 3) {

                if(args[0].equals("create")) {

                    TeamType teamType = TeamType.getByName(args[2]);
                    ItemSpawnerType itemSpawnerType = ItemSpawnerType.getByName(args[1]);

                    if(itemSpawnerType == null) {
                        Messanger.sendWarning(player, "Niepoprawny typ itemspawnera");
                        return true;
                    }
                    
                    if(itemSpawnerType.equals(ItemSpawnerType.DIAMOND) || itemSpawnerType.equals(ItemSpawnerType.EMERALD)) {
                        Messanger.sendWarning(player, "Niepoprawny typ itemspawnera");
                        return true;
                    }

                    if(teamType == null) {
                        Messanger.sendWarning(player, "Niepoprawny typ drużyny");
                        return true;
                    }

                    if(GameManager.presentGame(player.getWorld())) {

                        ItemSpawner itemSpawner = ItemSpawner.getBasicItemSpawner(player.getLocation());

                        Game game = GameManager.getGame(player.getWorld());
                        game.getTeam(teamType).setFurnace(itemSpawner);

                        Messanger.sendConfirm(player, "Utworzono nowy itemspawner");

                    } else {

                        Messanger.sendWarning(player, "Nie udało się utworzyć itemspawnera");

                    }

                }

            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completer = new ArrayList<String>();

        if(args.length == 1) {

            if("remove".startsWith(args[0])) completer.add("remove");
            if("create".startsWith(args[0])) completer.add("create");
            
        } else if(args.length == 2) {

            if(args[0].equals("create")) {
                if("basic".startsWith(args[1])) completer.add("basic");
                if("diamond".startsWith(args[1])) completer.add("diamond");
                if("emerald".startsWith(args[1])) completer.add("emerald");
            }

        } else if(args.length == 3) {

            if(args[0].equals("create")) {
                if(args[1].equals("basic")) {
                    for(TeamType type : TeamType.values()) {
                        if(type.name().toLowerCase().startsWith(args[2])) completer.add(type.name().toLowerCase());
                    }
                }
            }

        }

        return completer;
    }

}