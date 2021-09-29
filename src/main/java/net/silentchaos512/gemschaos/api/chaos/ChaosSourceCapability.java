package net.silentchaos512.gemschaos.api.chaos;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.config.ChaosConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChaosSourceCapability implements IChaosSource, ICapabilitySerializable<CompoundTag> {
    @CapabilityInject(IChaosSource.class)
    public static Capability<IChaosSource> INSTANCE = null;
    public static ResourceLocation NAME = ChaosMod.getId("chaos_source");

    private static final String NBT_CHAOS = "Chaos";

    private final LazyOptional<IChaosSource> holder = LazyOptional.of(() -> this);

    private int chaos;

    @Override
    public int getChaos() {
        return chaos;
    }

    @Override
    public void setChaos(int amount) {
        chaos = Mth.clamp(amount, 0, ChaosConfig.Common.maxChaos.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (INSTANCE == null) {
            String message = "ChaosSourceCapability.INSTANCE is null!";
            ChaosMod.LOGGER.fatal(message);
            throw new IllegalStateException(message);
        }
        return INSTANCE.orEmpty(cap, holder);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(NBT_CHAOS, chaos);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        chaos = nbt.getInt(NBT_CHAOS);
    }

    public static boolean canAttachTo(ICapabilityProvider obj) {
        return (obj instanceof Player || obj instanceof Level) && !obj.getCapability(INSTANCE).isPresent();
    }
}
