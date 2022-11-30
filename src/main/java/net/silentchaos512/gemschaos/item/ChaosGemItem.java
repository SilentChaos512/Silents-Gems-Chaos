package net.silentchaos512.gemschaos.item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gems.util.IGem;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.api.ChaosApi;
import net.silentchaos512.gemschaos.api.WorldPos;
import net.silentchaos512.gemschaos.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gemschaos.api.pedestal.PedestalItemCapability;
import net.silentchaos512.gemschaos.api.pedestal.PedestalItemImpl;
import net.silentchaos512.gemschaos.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gemschaos.chaosbuff.IChaosBuff;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.Item.Properties;

public class ChaosGemItem extends Item implements IGem {
    private static final String NBT_ENABLED = "Enabled";
    private static final String NBT_BUFF_LIST = "Buffs";
    private static final String NBT_BUFF_KEY = "ID";
    private static final String NBT_BUFF_LEVEL = "Level";

    private static final int MAX_SLOTS = 20;
    private static final int PEDESTAL_RANGE = 8;

    private final Gems gem;

    public ChaosGemItem(Gems gem, Properties properties) {
        super(properties);
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if (cap == PedestalItemCapability.INSTANCE) {
                    return LazyOptional.of(() -> new PedestalItemImpl(
                            ChaosGemItem::pedestalPowerChange,
                            ChaosGemItem::pedestalTick
                    )).cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    //region Buffs and chaos

    public static Map<IChaosBuff, Integer> getBuffs(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTag() || !stack.getOrCreateTag().contains(NBT_BUFF_LIST)) {
            return new HashMap<>();
        }

        ListTag tagList = stack.getOrCreateTag().getList(NBT_BUFF_LIST, 10);
        Map<IChaosBuff, Integer> map = new LinkedHashMap<>();

        for (Tag nbt : tagList) {
            if (nbt instanceof CompoundTag) {
                CompoundTag tag = (CompoundTag) nbt;
                String key = tag.getString(NBT_BUFF_KEY);
                int level = tag.getInt(NBT_BUFF_LEVEL);
                IChaosBuff buff = ChaosBuffManager.get(key);
                if (buff != null) {
                    map.put(buff, level);
                }
            }
        }

        return map;
    }

    public static boolean addBuff(ItemStack stack, IChaosBuff buff) {
        if (canAddBuff(stack, buff)) {
            Map<IChaosBuff, Integer> buffMap = getBuffs(stack);
            int currentLevel = 0;
            if (buffMap.containsKey(buff)) {
                currentLevel = buffMap.get(buff);
            }
            buffMap.put(buff, currentLevel + 1);
            setBuffs(stack, buffMap);

            return true;
        }

        return false;
    }

    public static void setBuffs(ItemStack stack, Map<IChaosBuff, Integer> buffs) {
        if (stack.isEmpty()) return;

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(NBT_BUFF_LIST)) {
            tag.remove(NBT_BUFF_LIST);
        }

        ListTag tagList = new ListTag();

        for (Map.Entry<IChaosBuff, Integer> entry : buffs.entrySet()) {
            IChaosBuff buff = entry.getKey();
            int level = entry.getValue();
            CompoundTag compound = new CompoundTag();
            compound.putString(NBT_BUFF_KEY, buff.getId().toString());
            compound.putShort(NBT_BUFF_LEVEL, (short) level);
            tagList.add(compound);
        }

        tag.put(NBT_BUFF_LIST, tagList);
    }

    public static boolean canAddBuff(ItemStack stack, IChaosBuff buff) {
        if (stack.isEmpty()) return false;
        if (!stack.hasTag()) return true;

        Map<IChaosBuff, Integer> buffMap = getBuffs(stack);

        if (buffMap.containsKey(buff)) {
            // We already have this buff, but might be able to increment
            int currentLevel = buffMap.get(buff);
            if (currentLevel >= buff.getMaxLevel()) {
                return false;
            }
            buffMap.put(buff, currentLevel + 1);
        } else {
            // We don't have the buff
            buffMap.put(buff, 1);
        }

        // Not exceeding max slots?
        return getSlotsUsed(buffMap) <= MAX_SLOTS;
    }

    public static int getSlotsUsed(ItemStack stack) {
        return getSlotsUsed(getBuffs(stack));
    }

    public static int getSlotsUsed(Map<IChaosBuff, Integer> buffMap) {
        return buffMap.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getSlotsForLevel(entry.getValue()))
                .sum();
    }

    public static int getChaosGenerated(ItemStack stack, @Nullable Player player) {
        return getBuffs(stack).entrySet().stream()
                .mapToInt(entry -> entry.getKey().getChaosGenerated(player, entry.getValue()))
                .sum();
    }

    public static int getMaxChaosGenerated(ItemStack stack) {
        return getBuffs(stack).entrySet().stream()
                .mapToInt(entry -> entry.getKey().getActiveChaosGenerated(entry.getValue()))
                .sum();
    }

    public static boolean isEnabled(ItemStack stack) {
        return !stack.isEmpty() && stack.getOrCreateTag().getBoolean(NBT_ENABLED);
    }

    public static void setEnabled(ItemStack stack, boolean value) {
        if (stack.isEmpty()) return;
        stack.getOrCreateTag().putBoolean(NBT_ENABLED, value);
    }

    public static int getBuffLevel(ItemStack stack, IChaosBuff buff) {
        Map<IChaosBuff, Integer> buffMap = getBuffs(stack);
        return buffMap.getOrDefault(buff, 0);
    }

    public static void applyEffects(ItemStack stack, Player player) {
        getBuffs(stack).forEach((buff, level) -> buff.applyTo(player, level));
    }

    public static void removeEffects(ItemStack stack, Player player) {
        getBuffs(stack).keySet().forEach(buff -> buff.removeFrom(player));
    }

    //endregion

    //region Pedestal cap

    public static boolean pedestalPowerChange(ItemStack stack, WorldPos pos, boolean powered) {
        setEnabled(stack, powered);
        return true;
    }

    public static void pedestalTick(ItemStack stack, WorldPos pos) {
        if (pos.getWorld().isClientSide || !isEnabled(stack)) return;

        int totalChaos = 0;
        AABB boundingBox = new AABB(pos.getPos()).inflate(PEDESTAL_RANGE);
        List<Player> players = pos.getWorld().getEntitiesOfClass(Player.class, boundingBox);
        for (Player player : players) {
            applyEffects(stack, player);
            totalChaos += getChaosGenerated(stack, player);
        }

        // Generate chaos, but scale back with more players
        float divisor = 1 + 0.25f * (players.size() - 1);
        int discountedChaos = (int) (totalChaos / divisor);
        ChaosApi.Chaos.generate(pos.getWorld(), discountedChaos, pos.getPos());
    }

    //endregion

    //region Item overrides

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isClientSide || !(entity instanceof Player)) return;

        // Apply effects?
        if (isEnabled(stack)) {
            Player player = (Player) entity;
            applyEffects(stack, player);
            ChaosApi.Chaos.generate(player, getChaosGenerated(stack, player), true);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        setEnabled(stack, false);
        removeEffects(stack, player);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.isEmpty()) {
            setEnabled(stack, !isEnabled(stack));

            if (isEnabled(stack)) {
                applyEffects(stack, player);
            } else {
                removeEffects(stack, player);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        getBuffs(stack).forEach((buff, level) -> tooltip.add(buff.getDisplayName(level)));
        tooltip.add(ChaosMod.TEXT.translate("item", "chaos_gem.slots", getSlotsUsed(stack), MAX_SLOTS));
        tooltip.add(chaosGenTooltip("chaos", getChaosGenerated(stack, ChaosMod.PROXY.getClientPlayer())));
        tooltip.add(chaosGenTooltip("chaosMax", getMaxChaosGenerated(stack)));
    }

    private static Component chaosGenTooltip(String key, int chaos) {
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaos);
        return ChaosMod.TEXT.translate("item", "chaos_gem." + key, emissionRate.getDisplayName(chaos));
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public Component getDescription() {
        return ChaosMod.TEXT.translate("item", "chaos_gem", this.gem.getDisplayName());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isEnabled(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        boolean oldEnabled = isEnabled(oldStack);
        boolean newEnabled = isEnabled(newStack);
        return slotChanged || oldEnabled != newEnabled || !oldStack.sameItem(newStack);
    }

    //endregion
}
