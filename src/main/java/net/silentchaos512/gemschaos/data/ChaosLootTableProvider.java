package net.silentchaos512.gemschaos.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.gemschaos.setup.ChaosRegistration;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChaosLootTableProvider extends LootTableProvider {
    public ChaosLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(BlockLootTables::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTables.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLoot {
        @Override
        protected void addTables() {
            add(ChaosBlocks.CHAOS_ORE.get(), BlockLootTables::chaosOreDrops);
            add(ChaosBlocks.DEEPSLATE_CHAOS_ORE.get(), BlockLootTables::chaosOreDrops);

            dropSelf(ChaosBlocks.CHAOS_CRYSTAL_BLOCK.get());

            add(ChaosBlocks.CORRUPTED_STONE.get(), b ->
                    createSingleItemTableWithSilkTouch(b, ChaosItems.CORRUPTED_STONE_PILE, ConstantValue.exactly(4)));
            add(ChaosBlocks.CORRUPTED_DIRT.get(), b ->
                    createSingleItemTableWithSilkTouch(b, ChaosItems.CORRUPTED_DIRT_PILE, ConstantValue.exactly(4)));
        }

        private static LootTable.Builder chaosOreDrops(Block block) {
            return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(ChaosItems.CHAOS_CRYSTAL)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 4)))
                    .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ChaosRegistration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}
