package net.silentchaos512.gemschaos.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.data.chaosbuff.ChaosBuffsProvider;
import net.silentchaos512.gemschaos.data.client.ChaosBlockStateProvider;
import net.silentchaos512.gemschaos.data.client.ChaosItemModelProvider;
import net.silentchaos512.gemschaos.data.gear.ChaosMaterialsProvider;
import net.silentchaos512.gemschaos.data.gear.ChaosTraitsProvider;
import net.silentchaos512.gemschaos.data.recipe.ChaosRecipeProvider;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ChaosBlockTagsProvider blockTags = new ChaosBlockTagsProvider(gen, existingFileHelper);
        gen.addProvider(true, blockTags);
        gen.addProvider(true, new ChaosItemTagsProvider(gen, blockTags, existingFileHelper));
        gen.addProvider(true, new ChaosRecipeProvider(gen));
        gen.addProvider(true, new ChaosLootTableProvider(gen));
//        gen.addProvider(true, new GemsAdvancementProvider(gen));

        gen.addProvider(false, new ChaosTraitsProvider(gen));
        gen.addProvider(false, new ChaosMaterialsProvider(gen));

        gen.addProvider(true, new ChaosBuffsProvider(gen, ChaosMod.MOD_ID));

        gen.addProvider(true, new ChaosBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(true, new ChaosItemModelProvider(gen, existingFileHelper));

        ChaosWorldGen.init(gen, existingFileHelper);
    }
}
