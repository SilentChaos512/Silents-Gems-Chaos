package net.silentchaos512.gemschaos.api.pedestal;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PedestalItemCapability {
    @CapabilityInject(IPedestalItem.class)
    public static Capability<IPedestalItem> INSTANCE = null;

    private PedestalItemCapability() {}
}
