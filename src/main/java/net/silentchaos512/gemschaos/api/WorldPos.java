package net.silentchaos512.gemschaos.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class WorldPos {
    private final Level world;
    private final BlockPos pos;

    public WorldPos(Level world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public Level getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }
}
