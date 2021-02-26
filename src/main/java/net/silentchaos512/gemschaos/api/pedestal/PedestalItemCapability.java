package net.silentchaos512.gemschaos.api.pedestal;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class PedestalItemCapability {
    @CapabilityInject(IPedestalItem.class)
    public static Capability<IPedestalItem> INSTANCE = null;

    private PedestalItemCapability() {}

    public static void register() {
        CapabilityManager.INSTANCE.register(IPedestalItem.class,
                new Capability.IStorage<IPedestalItem>() {
                    @Override
                    public INBT writeNBT(Capability<IPedestalItem> capability, IPedestalItem instance, Direction side) {
                        return new CompoundNBT();
                    }

                    @Override
                    public void readNBT(Capability<IPedestalItem> capability, IPedestalItem instance, Direction side, INBT nbt) {
                    }
                },
                PedestalItem::new);
    }
}
