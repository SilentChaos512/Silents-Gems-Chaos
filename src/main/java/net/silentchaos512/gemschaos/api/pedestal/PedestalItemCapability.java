package net.silentchaos512.gemschaos.api.pedestal;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PedestalItemCapability {
    public static final Capability<IPedestalItem> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    private PedestalItemCapability() {}
}
