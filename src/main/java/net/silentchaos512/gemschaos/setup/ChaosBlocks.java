package net.silentchaos512.gemschaos.setup;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChaosBlocks {
    private static final Collection<BlockRegistryObject<? extends Block>> SIMPLE_BLOCKS = new ArrayList<>();

    public static final BlockRegistryObject<Block> CHAOS_ORE = registerSimple("chaos_ore", () ->
            getChaosOre(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4, 10)
                    .requiresCorrectToolForDrops()));
    public static final BlockRegistryObject<Block> DEEPSLATE_CHAOS_ORE = registerSimple("deepslate_chaos_ore", () ->
            getChaosOre(BlockBehaviour.Properties.copy(CHAOS_ORE.get())
                    .strength(5, 15)
                    .sound(SoundType.DEEPSLATE)));

    public static final BlockRegistryObject<Block> CHAOS_CRYSTAL_BLOCK = registerSimple("chaos_crystal_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(4, 30)
                    .sound(SoundType.METAL)));

    public static final BlockRegistryObject<Block> CORRUPTED_STONE = registerSimple("corrupted_stone", ChaosBlocks::createCorruptedBlock);
    public static final BlockRegistryObject<Block> CORRUPTED_DIRT = registerSimple("corrupted_dirt", ChaosBlocks::createCorruptedBlock);

    private ChaosBlocks() {}

    public static void register() {}

    @OnlyIn(Dist.CLIENT)
    static void registerRenderTypes(FMLClientSetupEvent event) {
    }

    public static Collection<BlockRegistryObject<? extends Block>> getSimpleBlocks() {
        return Collections.unmodifiableCollection(SIMPLE_BLOCKS);
    }

    private static Block createCorruptedBlock() {
        return new Block(BlockBehaviour.Properties.of(Material.CLAY)
                .strength(1)
                .sound(SoundType.GRAVEL)
                .lightLevel(state -> 7));
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(ChaosRegistration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, ChaosBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        BlockRegistryObject<T> ret = registerNoItem(name, block);
        ChaosRegistration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static <T extends Block> BlockRegistryObject<T> registerSimple(String name, Supplier<T> block) {
        BlockRegistryObject<T> ret = register(name, block);
        SIMPLE_BLOCKS.add(ret);
        return ret;
    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().tab(ChaosMod.ITEM_GROUP));
    }

    private static Block getChaosOre(final BlockBehaviour.Properties properties) {
        return new Block(properties) {
            @Override
            public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silkTouch) {
                return silkTouch == 0 ? Mth.nextInt(RANDOM, 2, 7) : 0;
            }
        };
    }
}
