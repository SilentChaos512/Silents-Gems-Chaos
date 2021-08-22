package net.silentchaos512.gemschaos.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import net.silentchaos512.gems.util.Const;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.gemschaos.compat.tokenenchanter.TokenEnchanterCompat;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class ChaosXpCrystalItem extends PlayerLinkedItem {
    public ChaosXpCrystalItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return TokenEnchanterCompat.getChaosXpStorageProvider(stack, nbt);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!ModList.get().isLoaded(Const.TOKEN_ENCHANTER_MOD_ID)) {
            tooltip.add(TextUtil.itemSub(this, "modNotInstalled").withStyle(TextFormatting.RED));
        } else {
            tooltip.add(TextUtil.itemSub(this, "desc").withStyle(TextFormatting.ITALIC));
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
