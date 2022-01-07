package com.blackmc.blackbedwars.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.core.GameState;
import com.blackmc.blackbedwars.core.GameType;
import com.blackmc.blackbedwars.core.TeamType;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class GameCmd implements CommandExecutor, TabCompleter {

    public GameCmd(BlackBedWars plugin) {
        plugin.getCommand("game").setExecutor(this);
        plugin.getCommand("game").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(args.length == 0) {

                Messanger.sendWarning(player, "Wpisz więcej argumentów");

            } else if(args.length == 1) {

                if(args[0].equals("setlobbyspawn")) {

                    Game game = GameManager.getGame(player.getWorld());

                    if(game != null)
                        game.setSpawn(player.getLocation());
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ustawiać spawny");

                } else if(args[0].equals("setspectatorspawn")) {

                    Game game = GameManager.getGame(player.getWorld());

                    if(game != null)
                        game.setSpectatorSpawn(player.getLocation());
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ustawiać spawny");

                } else if(args[0].equals("init")) {

                    Game game = GameManager.getGame(player.getWorld());

                    if(game != null)
                        if(game.getState() == GameState.WAITING || game.getState() == GameState.RUNNING) {
                            Messanger.sendWarning(player, "Gra jest już zainicjalizowana lub trwa");
                        } else if(game.init()) {
                            Messanger.sendConfirm(player, "Zainicjalizowałeś grę");
                        } else {
                            Messanger.sendWarning(player, "Nie wszystkie spawny są ustawione");
                        }
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ją zainicjalizować");

                } else if(args[0].equals("uninit")) {

                    Game game = GameManager.getGame(player.getWorld());

                    if(game != null)
                        if(game.getState() == GameState.STOPPED || game.getState() == GameState.RUNNING) {
                            Messanger.sendWarning(player, "Gra nie jest zainicjalizowana");
                        } else if(game.init()) {
                            Messanger.sendConfirm(player, "Zainicjalizowałeś grę");
                        } else {
                            Messanger.sendWarning(player, "Nie wszystkie spawny są ustawione");
                        }
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ją zainicjalizować");

                } else if(args[0].equals("list")) {

                    Messanger.sendConfirm(player, "Lista dostępnych gier:");

                    List<Game> games = new ArrayList<Game>(GameManager.getGames().values());
                    Collections.sort(games, Comparator.comparing(Game::isInitialized));
                    Collections.reverse(games);

                    for(Game game : games) {
                        
                        Messanger.sendMessage(player,
                            (game.getState().equals(GameState.RUNNING) ? ChatColor.YELLOW + "?" : game.getState().equals(GameState.WAITING) ? ChatColor.GREEN + "+" : ChatColor.RED + "-")
                            + " " + game.getName());

                    }

                } else if(args[0].equals("save")) {

                    Game game = GameManager.getGame(player.getWorld());

                    if(game != null)
                        if(game.getState() == GameState.WAITING || game.getState() == GameState.RUNNING) {
                            Messanger.sendWarning(player, "Nie możesz zapisać stanu świata podczas gry");
                        } else {
                            Messanger.sendConfirm(player, "Zapisałeś stan świata dla tej rozgrywki");
                        }
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ją zapisać");

                } else if(args[0].equals("removenpc")) {

                    GameManager.addRemovingNpc(player);
                    Messanger.sendMessage(player, "Wybierz NPC, któremu chcesz usunąć przypisanie do gier");

                } else {

                    Messanger.sendWarning(player, "Wprowadziłeś złe argumenty");

                }
                
            } else if(args.length == 2) {

                if(args[0].equals("setnpc")) {

                    String name = args[1];
                    GameType type = GameType.getByName(name);

                    if(type != null) {
                        GameManager.addSettingNpc(player, type);
                        Messanger.sendMessage(player, "Wybierz NPC, któremu chcesz ustawić przypisanie do gier typu: " + ChatColor.YELLOW + type);
                    } else {
                        Messanger.sendWarning(player, "Nie istnieje typ gry o podanej nazwie");
                    }

                } else if(args[0].equals("setteamspawn")) {

                    String name = args[1];

                    Game game = GameManager.getGame(player.getWorld());
                    TeamType type = TeamType.getByName(name);

                    if(game != null)
                        if(type != null)
                            game.setTeamSpawn(player.getLocation(), type);
                        else
                            Messanger.sendWarning(player, "Nie znaleziono takiego teamu");
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ustawiać spawny");

                } else if(args[0].equals("setteambed")) {

                    String name = args[1];

                    Game game = GameManager.getGame(player.getWorld());
                    TeamType type = TeamType.getByName(name);

                    if(game != null)
                        if(type != null) {
                            GameManager.addSettingBed(player, type);
                            Messanger.sendMessage(player, "Wybierz łóżko, które chcesz ustawić dla tej drużyny");
                        } else {
                            Messanger.sendWarning(player, "Nie znaleziono takiego teamu");
                        }
                    else
                        Messanger.sendWarning(player, "Musisz znajdować się na świecie z grą, aby móc ustawiać spawny");

                } else if(args[0].equals("create")) {

                    String name = args[1];

                    Messanger.sendMessage(player, "Tworzenie świata...");
                    boolean created = GameManager.createGame(name);

                    if(created)
                        Messanger.sendConfirm(player, "Stworzono nowy świat o podanej nazwie");
                    else
                        Messanger.sendWarning(player, "Błąd w tworzeniu świata (prawdopodobnie jest już taka gra lub świat)");

                } else if(args[0].equals("import")) {

                    String name = args[1];

                    Messanger.sendMessage(player, "Importowanie świata...");
                    boolean imported = GameManager.importGame(name);

                    if(imported)
                        Messanger.sendConfirm(player, "Zaimportowano nowy świat o podanej nazwie");
                    else
                        Messanger.sendWarning(player, "Błąd w importowaniu świata (prawdopodobnie jest już taka gra lub świat o takiej nazwie nie istnieje)");

                } else if(args[0].equals("remove")) {

                    String name = args[1];
                    boolean removed = GameManager.removeGame(name);

                    if(removed)
                        Messanger.sendConfirm(player, "Usunięto świat o podanej nazwie");
                    else
                        Messanger.sendWarning(player, "Gra o takiej nazwie nie istnieje");

                } else {

                    Messanger.sendWarning(player, "Wprowadziłeś złe argumenty");

                }

            } else if(args.length == 3) {

                if(args[0].equals("create")) {

                    String name = args[1];
                    String type = args[2];

                    if(type.equals("1v1") || type.equals("2v2") || type.equals("3v3") || type.equals("4v4")) {

                        boolean created = GameManager.createGame(name, (type.equals("1v1") ? GameType.ONES : (type.equals("2v2") ? GameType.DOUBLES : (type.equals("3v3") ? GameType.TRIPPLES : GameType.QUADRUPLES))));
    
                        if(created)
                            Messanger.sendConfirm(player, "Stworzono nowy świat o podanej nazwie");
                        else
                            Messanger.sendWarning(player, "Błąd w importowaniu świata (prawdopodobnie jest już taka gra lub świat o takiej nazwie nie istnieje)");

                    } else {
                        Messanger.sendWarning(player, "Podałeś zły rodzaj gry");
                    }

                } else if(args[0].equals("import")) {

                    String name = args[1];
                    String type = args[2];

                    if(type.equals("1v1") || type.equals("2v2") || type.equals("3v3") || type.equals("4v4")) {

                        boolean imported = GameManager.importGame(name, (type.equals("1v1") ? GameType.ONES : (type.equals("2v2") ? GameType.DOUBLES : (type.equals("3v3") ? GameType.TRIPPLES : GameType.QUADRUPLES))));

                        if(imported)
                            Messanger.sendConfirm(player, "Zaimportowano nowy świat o podanej nazwie");
                        else
                            Messanger.sendWarning(player, "Błąd w importowaniu świata (prawdopodobnie jest już taka gra lub świat o takiej nazwie nie istnieje)");

                    } else {
                        Messanger.sendWarning(player, "Podałeś zły rodzaj gry");
                    }

                } else {

                    Messanger.sendWarning(player, "Wprowadziłeś złe argumenty");

                }

            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completer = new ArrayList<String>();

        if(sender.isOp()) { //* change to hasPermission

            if(args.length == 1) {

                if("create".startsWith(args[0])) completer.add("create");
                if("import".startsWith(args[0])) completer.add("import");
                if("remove".startsWith(args[0])) completer.add("remove");
                if("setnpc".startsWith(args[0])) completer.add("setnpc");
                if("removenpc".startsWith(args[0])) completer.add("removenpc");
                if("setspectatorspawn".startsWith(args[0])) completer.add("setspectatorspawn");
                if("setlobbyspawn".startsWith(args[0])) completer.add("setlobbyspawn");
                if("setteamspawn".startsWith(args[0])) completer.add("setteamspawn");
                if("setteambed".startsWith(args[0])) completer.add("setteambed");
                if("init".startsWith(args[0])) completer.add("init");

            } else if(args.length == 2) {

                if(args[0].equals("create") || args[0].equals("import")) {

                    for(Game game : GameManager.getGames().values()) {
                        if(game.getName().startsWith(args[1])) completer.add(game.getName());
                    }

                } else if(args[0].equals("setteamspawn") || args[0].equals("setteambed")) {

                    for(TeamType type : TeamType.values()) {
                        if(type.name().toLowerCase().startsWith(args[1])) completer.add(type.name().toLowerCase());
                    }

                } if(args[0].equals("setnpc")) {

                    for(GameType type : GameType.values()) {
                        if(type.toString().toLowerCase().startsWith(args[1])) completer.add(type.toString().toLowerCase());
                    }

                }

            }

        }

        return completer;
    }
    
}