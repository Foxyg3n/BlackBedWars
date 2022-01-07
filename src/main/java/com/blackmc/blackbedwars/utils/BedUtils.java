package com.blackmc.blackbedwars.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;

public class BedUtils {

    public static boolean isBedBlock(Block block) {
        return block.getBlockData() instanceof Bed;
    }

    public static boolean isBedHead(Block block) {
        return ((Bed) block.getBlockData()).getPart().equals(Part.HEAD);
    }

    public static boolean setBed(Block feet, Block head, Material bedColor) {

        BlockFace face = head.getFace(feet);

        if(face == null || feet.getBlockData() instanceof Bed || head.getBlockData() instanceof Bed) return false;
        else if(face.equals(BlockFace.UP) || face.equals(BlockFace.DOWN) || face.equals(BlockFace.SELF)) return false;

        for (Bed.Part part : Bed.Part.values()) {
            feet.setType(bedColor);
            final Bed bedState = (Bed) feet.getBlockData();
            bedState.setPart(part);
            bedState.setFacing(face);
            feet.setBlockData(bedState);
            feet = feet.getRelative(face.getOppositeFace());
        }

        return true;
    }
    
}
