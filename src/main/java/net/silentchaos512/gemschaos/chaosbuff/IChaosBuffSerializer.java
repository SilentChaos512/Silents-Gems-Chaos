package net.silentchaos512.gemschaos.chaosbuff;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public interface IChaosBuffSerializer<T extends IChaosBuff> {
    T deserialize(ResourceLocation id, JsonObject json);

    T read(ResourceLocation id, PacketBuffer buffer);

    void write(PacketBuffer buffer, T trait);

    ResourceLocation getName();
}
