package net.silentchaos512.gemschaos.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.silentchaos512.gemschaos.ChaosMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class PlayerLinkedItem extends Item {
    private static final String NBT_PLAYER = "Player";

    public PlayerLinkedItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static Player getOwner(ItemStack stack, EntityGetter world) {
        UUID uuid = getOwnerUuid(stack);
        if (uuid != null) {
            return world.getPlayerByUUID(uuid);
        }
        return null;
    }

    @Nullable
    public static UUID getOwnerUuid(ItemStack stack) {
        if (stack.getOrCreateTag().contains(NBT_PLAYER)) {
            return stack.getOrCreateTag().getUUID(NBT_PLAYER);
        }
        return null;
    }

    public static void setOwner(ItemStack stack, Player player) {
        stack.getOrCreateTag().putUUID(NBT_PLAYER, player.getUUID());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        UUID ownerUuid = getOwnerUuid(stack);

        if (worldIn != null) {
            Player owner = getOwner(stack, worldIn);

            if (ownerUuid != null) {
                tooltip.add(getOwnerText(ownerUuid, owner));
            }
        }
    }

    private static Component getOwnerText(UUID ownerUuid, @Nullable Player owner) {
        Component text = owner != null
                ? Component.literal(owner.getScoreboardName()).withStyle(ChatFormatting.GREEN)
                : Component.literal(ownerUuid.toString()).withStyle(ChatFormatting.RED);
        return ChaosMod.TEXT.translate("item", "chaos_linker.owner", text);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        UUID ownerUuid = getOwnerUuid(stack);

        if (ownerUuid == null) {
            if (playerIn.isShiftKeyDown()) {
                setOwner(stack, playerIn);
                return InteractionResultHolder.success(stack);
            } else {
                playerIn.displayClientMessage(ChaosMod.TEXT.translate("item", "chaos_linker.notSneaking"), true);
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}
