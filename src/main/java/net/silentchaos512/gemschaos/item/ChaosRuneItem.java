package net.silentchaos512.gemschaos.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.silentchaos512.gemschaos.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gemschaos.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gemschaos.chaosbuff.IChaosBuff;
import net.silentchaos512.gemschaos.chaosbuff.PotionChaosBuff;
import net.silentchaos512.gemschaos.setup.ChaosItems;
import net.silentchaos512.utils.Color;

import javax.annotation.Nullable;
import java.util.List;

public final class ChaosRuneItem extends Item {
    private static final String NBT_KEY = "SGems_BuffRune";

    public ChaosRuneItem(Properties properties) {
        super(properties);
    }

    @Deprecated
    public static ItemStack getStack(IChaosBuff buff) {
        return getStack(buff.getId());
    }

    public static ItemStack getStack(ResourceLocation buffId) {
        ItemStack result = new ItemStack(ChaosItems.CHAOS_RUNE);
        result.getOrCreateTag().putString(NBT_KEY, buffId.toString());
        return result;
    }

    @Nullable
    public static IChaosBuff getBuff(ItemStack stack) {
        String string = stack.getOrCreateTag().getString(NBT_KEY);
        return ChaosBuffManager.get(string);
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) return Color.VALUE_WHITE;
        IChaosBuff buff = getBuff(stack);
        if (buff == null) return Color.VALUE_WHITE;
        return buff.getRuneColor();
    }

    @Override
    public Component getName(ItemStack stack) {
        IChaosBuff buff = getBuff(stack);
        if (buff != null) {
            Component buffName = buff.getDisplayName(0);
            return Component.translatable(this.getDescriptionId() + ".nameProper", buffName);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        IChaosBuff buff = getBuff(stack);
        if (buff == null) return;

        tooltip.add(Component.translatable(this.getDescriptionId() + ".maxLevel", buff.getMaxLevel()));
        tooltip.add(Component.translatable(this.getDescriptionId() + ".slotsUsed", buff.getSlotsForLevel(1)));
        int activeChaosGenerated = buff.getActiveChaosGenerated(1);
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(activeChaosGenerated);
        tooltip.add(Component.translatable(this.getDescriptionId() + ".chaos", emissionRate.getDisplayName(activeChaosGenerated)));

        // Debug
        if (flagIn.isAdvanced()) {
            tooltip.add(Component.literal(String.format("Buff ID: %s", buff.getId())).withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.literal(String.format("Color: %X", buff.getRuneColor())).withStyle(ChatFormatting.DARK_GRAY));
            if (buff instanceof PotionChaosBuff) {
                MobEffect effect = ((PotionChaosBuff) buff).getEffect();
                tooltip.add(Component.literal(String.format("Effect: %s", effect)).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
            for (IChaosBuff buff : ChaosBuffManager.getValues()) {
                items.add(getStack(buff.getId()));
            }
        }
    }
}

