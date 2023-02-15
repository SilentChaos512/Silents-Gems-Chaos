package net.silentchaos512.gemschaos.data.gear;

import net.minecraft.data.DataGenerator;
import net.silentchaos512.gear.api.data.trait.TraitBuilder;
import net.silentchaos512.gear.api.data.trait.TraitsProviderBase;
import net.silentchaos512.gemschaos.ChaosMod;

import java.util.ArrayList;
import java.util.Collection;

public class ChaosTraitsProvider extends TraitsProviderBase {
    public ChaosTraitsProvider(DataGenerator generator) {
        super(generator, ChaosMod.MOD_ID);
    }

    @Override
    public Collection<TraitBuilder> getTraits() {
        Collection<TraitBuilder> ret = new ArrayList<>();

        // TODO

        return ret;
    }
}
