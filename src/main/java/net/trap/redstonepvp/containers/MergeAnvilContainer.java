package me.drman.redstonepvp.containers;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;

public class MergeAnvilContainer extends ContainerAnvil {
    public MergeAnvilContainer(EntityHuman entityHuman) {
        super(entityHuman.inventory, entityHuman.world, new BlockPosition(0, 0, 0), entityHuman);
    }

    public boolean a(EntityHuman entityHuman) {
        return true;
    }
}