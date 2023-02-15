package net.silentchaos512.gemschaos.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.gemschaos.ChaosMod;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ChaosConfig {
    public static final class Common {
        static final ForgeConfigSpec spec;

        public static final ForgeConfigSpec.BooleanValue chaosNoEventsUntilHasBed;
        public static final ForgeConfigSpec.IntValue maxChaos;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

            {
                builder.comment("Settings related to the chaos system");
                builder.push("chaos");

                chaosNoEventsUntilHasBed = builder
                        .comment("Players will not experience chaos events until they have a respawn point set")
                        .define("noEventsUntilHasBed", true);

                maxChaos = builder
                        .comment("The maximum chaos value that a player can reach")
                        .defineInRange("maxChaos", 10_000_000, 1, Integer.MAX_VALUE);

                builder.pop();
            }

            spec = builder.build();
        }

        private Common() {}
    }

    private ChaosConfig() {}

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Common.spec);
    }
}
