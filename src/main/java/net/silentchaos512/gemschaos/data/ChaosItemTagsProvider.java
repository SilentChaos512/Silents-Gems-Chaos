package net.silentchaos512.gemschaos.data;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.gemschaos.setup.ChaosTags;

import javax.annotation.Nullable;

public class ChaosItemTagsProvider extends ItemTagsProvider {
    public ChaosItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, ChaosMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ChaosTags.Items.GEMS_CHAOS).add(ChaosItems.CHAOS_CRYSTAL.get());

        tag(ChaosTags.Items.DIRT)
                .add(Blocks.DIRT.asItem())
                .add(Blocks.GRASS_BLOCK.asItem())
                .add(Blocks.COARSE_DIRT.asItem())
                .add(Blocks.PODZOL.asItem())
                .add(Blocks.MYCELIUM.asItem());
    }
}
