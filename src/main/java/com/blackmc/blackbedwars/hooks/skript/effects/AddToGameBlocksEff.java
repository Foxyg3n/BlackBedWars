package com.blackmc.blackbedwars.hooks.skript.effects;

import com.blackmc.blackbedwars.core.Game;
import com.blackmc.blackbedwars.core.GameManager;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class AddToGameBlocksEff extends Effect {

    static {
        Skript.registerEffect(AddToGameBlocksEff.class, "add %block% to game blocks");
    }

    Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Add block to game blocks with expression block " + blockExpression.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Block block = blockExpression.getSingle(event);
        Game game = GameManager.getGame(block.getWorld().getName());
        if(game != null) game.addBlock(block);
    }
    
}
