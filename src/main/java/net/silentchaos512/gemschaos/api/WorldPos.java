package net.silentchaos512.gemschaos.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPos {
    private final World world;
    private final BlockPos pos;

    public WorldPos(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }
}
