package com.blackmc.blackbedwars.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.blackmc.blackbedwars.BlackBedWars;
import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;
import com.blackmc.blackbedwars.utils.Messanger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCmd implements CommandExecutor {

    public TestCmd(BlackBedWars plugin) {
        plugin.getCommand("test").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;
            Game game = GameManager.getGame(player.getWorld());

            if(game != null) {

                if(game.isInitialized()) {
                    game.startGame();
                } else {
                    Messanger.sendWarning(player, "Gra nie jest zainicjalizowana");
                }

            }

        }

        return true;
    }

    public static void main(String[] args) {

        ArrayList<Integer> testArray = new ArrayList<Integer>();
        testArray.add(12);
        testArray.add(13);
        testArray.add(7);
        testArray.add(6);
        testArray.add(2);
        testArray.add(15);

        Collections.sort(testArray, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }

        });

        testArray.stream().forEach(System.out::println);

    }
    
}