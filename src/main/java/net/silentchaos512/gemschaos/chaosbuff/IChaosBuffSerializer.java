package net.silentchaos512.gemschaos.chaosbuff;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IChaosBuffSerializer<T extends IChaosBuff> {
    T deserialize(ResourceLocation id, JsonObject json);

    T read(ResourceLocation id, FriendlyByteBuf buffer);

    void write(FriendlyByteBuf buffer, T trait);

    ResourceLocation getName();
}
