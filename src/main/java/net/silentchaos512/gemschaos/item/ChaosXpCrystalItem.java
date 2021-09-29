package net.silentchaos512.gemschaos.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import net.silentchaos512.gems.util.Const;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.gemschaos.compat.tokenenchanter.TokenEnchanterCompat;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ChaosXpCrystalItem extends PlayerLinkedItem {
    public ChaosXpCrystalItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return TokenEnchanterCompat.getChaosXpStorageProvider(stack, nbt);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!ModList.get().isLoaded(Const.TOKEN_ENCHANTER_MOD_ID)) {
            tooltip.add(TextUtil.itemSub(this, "modNotInstalled").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(TextUtil.itemSub(this, "desc").withStyle(ChatFormatting.ITALIC));
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
