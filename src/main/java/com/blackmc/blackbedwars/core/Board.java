package com.blackmc.blackbedwars.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import me.tade.quickboard.api.QuickBoardAPI;
import net.md_5.bungee.api.ChatColor;

public class Board {

    private Game game;
    private List<String> title = new ArrayList<String>();
    private List<String> inGameText = new ArrayList<String>();
    private List<String> inGameTextTeams = new ArrayList<String>();
    private List<String> lobbyText = new ArrayList<String>();
    private int updateTitle = Integer.MAX_VALUE;
    private int updateText = 20;
 
    public Board(Game game) {
        this.game = game;
        title.add(ChatColor.WHITE + "Black" + ChatColor.BLACK + "MC");
        
        // In-game text

        inGameText.add("");
        inGameText.add("Czas: %bbw_game_time%");
        inGameText.add("");

        if(game.getType().equals(GameType.ONES)) {
        
            inGameTextTeams.add(ChatColor.RED +  "" + ChatColor.BOLD + "C " + ChatColor.RESET + "Czerwoni: " + ChatColor.RESET + "%bbw_teamstate_red%");
            inGameTextTeams.add(ChatColor.GREEN +  "" + ChatColor.BOLD + "Z " + ChatColor.RESET + "Zieloni: " + ChatColor.RESET + "%bbw_teamstate_green%");
            inGameTextTeams.add(ChatColor.BLUE +  "" + ChatColor.BOLD + "N " + ChatColor.RESET + "Niebiescy: " + ChatColor.RESET + "%bbw_teamstate_blue%");
            inGameTextTeams.add(ChatColor.YELLOW +  "" + ChatColor.BOLD + "Ż " + ChatColor.RESET + "Żółci: " + ChatColor.RESET + "%bbw_teamstate_yellow%");
            inGameTextTeams.add(ChatColor.LIGHT_PURPLE +  "" + ChatColor.BOLD + "R " + ChatColor.RESET + "Różowi: " + ChatColor.RESET + "%bbw_teamstate_pink%");
            inGameTextTeams.add(ChatColor.GRAY +  "" + ChatColor.BOLD + "S " + ChatColor.RESET + "Szarzy: " + ChatColor.RESET + "%bbw_teamstate_gray%");
            inGameTextTeams.add(ChatColor.WHITE +  "" + ChatColor.BOLD + "B " + ChatColor.RESET + "Biali: " + ChatColor.RESET + "%bbw_teamstate_white%");
            inGameTextTeams.add(ChatColor.AQUA +  "" + ChatColor.BOLD + "A " + ChatColor.RESET + "Aqua: " + ChatColor.RESET + "%bbw_teamstate_aqua%");

        } else {
        
            inGameTextTeams.add(ChatColor.RED +  "" + ChatColor.BOLD + "C " + ChatColor.RESET + "Czerwoni: " + ChatColor.RESET + "%bbw_teamstate_red%");
            inGameTextTeams.add(ChatColor.GREEN +  "" + ChatColor.BOLD + "Z " + ChatColor.RESET + "Zieloni: " + ChatColor.RESET + "%bbw_teamstate_green%");
            inGameTextTeams.add(ChatColor.BLUE +  "" + ChatColor.BOLD + "N " + ChatColor.RESET + "Niebiescy: " + ChatColor.RESET + "%bbw_teamstate_blue%");
            inGameTextTeams.add(ChatColor.YELLOW +  "" + ChatColor.BOLD + "Ż " + ChatColor.RESET + "Żółci: " + ChatColor.RESET + "%bbw_teamstate_yellow%");

        }

        // Lobby text

        lobbyText.add("");
        lobbyText.add("Mapa" + ChatColor.BOLD + ">> " + ChatColor.RESET + "" + ChatColor.YELLOW + StringUtils.capitalize(game.getName()));
        lobbyText.add("Gracze" + ChatColor.BOLD + ">> " + ChatColor.RESET + "" + ChatColor.YELLOW + "%bbw_game_players%/" + game.getPlayerLimit());
        lobbyText.add("");
        lobbyText.add("Czas" + ChatColor.BOLD + ">> " + ChatColor.RESET + "" + ChatColor.YELLOW + "%bbw_game_state%");
        lobbyText.add("");
        lobbyText.add("Tryb" + ChatColor.BOLD + ">> " + ChatColor.RESET + "" + ChatColor.YELLOW + game.getType().getDisplayName());
        lobbyText.add("Drużyna" + ChatColor.BOLD + ">> " + ChatColor.RESET + "%bbw_teamname%");
        lobbyText.add("");

    }

    public void setInGameBoard(Player player) {
        QuickBoardAPI.removeBoard(player);
        ArrayList<String> board = createBoard(player);
        QuickBoardAPI.createBoard(player, board, title, updateTitle, updateText);
    }

    public void setLobbyBoard(Player player) {
        QuickBoardAPI.removeBoard(player);
        QuickBoardAPI.createBoard(player, lobbyText, title, updateTitle, updateText);
    }

    private ArrayList<String> createBoard(Player player) {
        ArrayList<String> board = new ArrayList<String>(inGameText);
        ArrayList<String> boardTeams = new ArrayList<String>(inGameTextTeams);
        TeamType type = game.getTeam(player).getType();
        for(int i = 0; i < boardTeams.size(); i++) {
            if(TeamType.values()[i].equals(type)) boardTeams.set(i, boardTeams.get(i) + ChatColor.GRAY + " TY"); //☭
        }
        board.addAll(boardTeams);

        return board;
    }

}