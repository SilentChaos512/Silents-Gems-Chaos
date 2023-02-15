package net.silentchaos512.gemschaos;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.gemschaos.api.chaos.IChaosSource;
import net.silentchaos512.gemschaos.api.pedestal.IPedestalItem;
import net.silentchaos512.gemschaos.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gemschaos.config.ChaosConfig;
import net.silentchaos512.gemschaos.network.ChaosNetwork;
import net.silentchaos512.gemschaos.setup.ChaosRegistration;
import net.silentchaos512.lib.event.Greetings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mod(ChaosMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChaosMod {
    public static final String MOD_ID = "silentgems_chaos";

    public static final Random RANDOM = new Random();
    public static final RandomSource RANDOM_SOURCE = RandomSource.create();
    public static final Logger LOGGER = LogManager.getLogger("Silent's Gems: Chaos");
    public static final TextUtil TEXT = new TextUtil(MOD_ID);

    public static ChaosProxy PROXY;

    public ChaosMod() {
        PROXY = DistExecutor.unsafeRunForDist(() -> ChaosProxy.Client::new, () -> ChaosProxy.Server::new);
        ChaosConfig.init();
        ChaosRegistration.register();
        ChaosNetwork.init();

        MinecraftForge.EVENT_BUS.addListener(ChaosMod::addReloadListeners);

        Greetings.addMessage(ChaosBuffManager::getGreetingErrorMessage);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IChaosSource.class);
        event.register(IPedestalItem.class);
    }

    private static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ChaosBuffManager.INSTANCE);
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "0.0.0";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion());
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Nullable
    public static ResourceLocation getIdWithDefaultNamespace(String name) {
        if (name.contains(":"))
            return ResourceLocation.tryParse(name);
        return ResourceLocation.tryParse(MOD_ID + ":" + name);
    }

    public static String shortenId(@Nullable ResourceLocation id) {
        if (id == null)
            return "null";
        if (MOD_ID.equals(id.getNamespace()))
            return id.getPath();
        return id.toString();
    }

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Gems.RUBY.getItem());
        }
    };
}
