package net.silentchaos512.gemschaos.chaosbuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.network.NetworkEvent;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.network.SyncChaosBuffsPacket;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public final class ChaosBuffManager implements ResourceManagerReloadListener {
    public static final ChaosBuffManager INSTANCE = new ChaosBuffManager();

    public static final Marker MARKER = MarkerManager.getMarker("ChaosBuffManager");

    private static final String DATA_PATH = "silentgems_chaos_buffs";
    private static final Map<ResourceLocation, IChaosBuff> MAP = Collections.synchronizedMap(new LinkedHashMap<>());

    private ChaosBuffManager() {
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(DATA_PATH, s -> s.toString().endsWith(".json"));
        if (resources.isEmpty()) return;

        synchronized (MAP) {
            MAP.clear();
            ChaosMod.LOGGER.info(MARKER, "Reloading chaos buff files");

            for (ResourceLocation id : resources.keySet()) {
                String path = id.getPath().substring(DATA_PATH.length() + 1, id.getPath().length() - ".json".length());
                ResourceLocation name = new ResourceLocation(id.getNamespace(), path);
                if (ChaosMod.LOGGER.isTraceEnabled()) {
                    ChaosMod.LOGGER.trace(MARKER, "Found likely chaos buff file: {}, trying to read as {}", id, name);
                }

                Optional<Resource> resourceOptional = resourceManager.getResource(id);
                if (resourceOptional.isPresent()) {
                    Resource iresource = resourceOptional.get();
                    JsonObject json = null;
                    try {
                        json = GsonHelper.fromJson(gson, IOUtils.toString(iresource.open(), StandardCharsets.UTF_8), JsonObject.class);
                    } catch (IOException e) {
                        ChaosMod.LOGGER.error(MARKER, "Could not read chaos buff {}", name, e);
                    }
                    if (json == null) {
                        ChaosMod.LOGGER.error(MARKER, "could not load chaos buff {} as it's null or empty", name);
                    } else if (json.has("conditions") && !CraftingHelper.processConditions(json.getAsJsonArray("conditions"), ICondition.IContext.EMPTY)) {
                        ChaosMod.LOGGER.info("Skipping loading chaos buff {} as it's conditions were not met", name);
                    } else {
                        addBuff(ChaosBuffSerializers.deserialize(name, json));
                    }
                }
            }

            ChaosMod.LOGGER.info(MARKER, "Finished! Registered {} chaos buffs", MAP.size());
        }
    }

    private static void addBuff(IChaosBuff buff) {
        if (MAP.containsKey(buff.getId())) {
            throw new IllegalArgumentException("Duplicate chaos buff " + buff.getId());
        }
        MAP.put(buff.getId(), buff);
    }

    @Nullable
    public static IChaosBuff get(ResourceLocation id) {
        synchronized (MAP) {
            return MAP.get(id);
        }
    }

    @Nullable
    public static IChaosBuff get(String id) {
        return get(new ResourceLocation(id));
    }

    public static Collection<IChaosBuff> getValues() {
        synchronized (MAP) {
            return MAP.values();
        }
    }

    public static void handlePacket(SyncChaosBuffsPacket packet, Supplier<NetworkEvent.Context> context) {
        synchronized (MAP) {
            MAP.clear();
            packet.getBuffs().forEach(buff -> MAP.put(buff.getId(), buff));
            ChaosMod.LOGGER.info("Received {} chaos buffs from server", MAP.size());
            context.get().setPacketHandled(true);
        }
    }

    @Nullable
    public static Component getGreetingErrorMessage(Player player) {
        synchronized (MAP) {
            if (MAP.isEmpty()) {
                ChaosMod.LOGGER.error("Something went wrong with chaos buff loading! This may be caused by another broken mod, even those not related to Silent's Gems.");
                return Component.literal("[Silent's Gems] No chaos buffs are loaded! This means chaos gems and runes will not work. This can be caused by a broken mod.");
            }
            return null;
        }
    }
}
