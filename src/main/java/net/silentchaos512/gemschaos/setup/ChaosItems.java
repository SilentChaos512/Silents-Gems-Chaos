package net.silentchaos512.gemschaos.setup;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.item.*;
import net.silentchaos512.lib.registry.ItemRegistryObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChaosItems {
    private static final Collection<ItemRegistryObject<? extends Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    public static final ItemRegistryObject<Item> CHAOS_CRYSTAL = registerCraftingItem("chaos_crystal");
    public static final ItemRegistryObject<Item> CHAOS_LINKER_CORE = registerCraftingItem("chaos_linker_core");
    public static final ItemRegistryObject<Item> RUNE_SLATE = registerCraftingItem("rune_slate");
    public static final ItemRegistryObject<Item> CORRUPTED_STONE_PILE = registerCraftingItem("corrupted_stone_pile");
    public static final ItemRegistryObject<Item> CORRUPTED_DIRT_PILE = registerCraftingItem("corrupted_dirt_pile");

    public static final ItemRegistryObject<ChaosLinkerItem> CHAOS_LINKER = registerSimpleModel("chaos_linker", () ->
            new ChaosLinkerItem(unstackableProps()));

    public static final ItemRegistryObject<ChaosFlintAndSteelItem> CHAOS_FLINT_AND_STEEL = registerSimpleModel("chaos_flint_and_steel", () ->
            new ChaosFlintAndSteelItem(unstackableProps().durability(8)));

    public static final ItemRegistryObject<ChaosOrbItem> CHAOS_POTATO = register("chaos_potato", () ->
            new ChaosOrbItem(0, 5000, 0.5f));
    public static final ItemRegistryObject<ChaosOrbItem> FRAGILE_CHAOS_ORB = register("fragile_chaos_orb", () ->
            new ChaosOrbItem(2, 100_000, 0.2f));
    public static final ItemRegistryObject<ChaosOrbItem> REFINED_CHAOS_ORB = register("refined_chaos_orb", () ->
            new ChaosOrbItem(4, 1_000_000, 0.1f));
    public static final ItemRegistryObject<ChaosOrbItem> PERFECT_CHAOS_ORB = register("perfect_chaos_orb", () ->
            new ChaosOrbItem(4, 10_000_000, 0.05f));

    public static final ItemRegistryObject<ChaosXpCrystalItem> CHAOS_XP_CRYSTAL = registerSimpleModel("chaos_xp_crystal", () ->
            new ChaosXpCrystalItem(unstackableProps()));

    public static final Map<Gems, ItemRegistryObject<ChaosGemItem>> CHAOS_GEMS = registerGemSetItem(
            gem -> "chaos_" + gem.getName(),
            gem -> new ChaosGemItem(gem, unstackableProps().rarity(Rarity.RARE))
    );

    public static final ItemRegistryObject<ChaosRuneItem> CHAOS_RUNE = register("chaos_rune", () ->
            new ChaosRuneItem(baseProps()));

    private ChaosItems() {}

    public static void register() {}

    public static Collection<ItemRegistryObject<? extends Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(ChaosRegistration.ITEMS.register(name, item));
    }

    private static <T extends Item> ItemRegistryObject<T> registerSimpleModel(String name, Supplier<T> item) {
        ItemRegistryObject<T> ret = register(name, item);
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static ItemRegistryObject<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        return registerSimpleModel(name, () -> new Item(baseProps()));
    }

    private static <T extends Item> Map<Gems, ItemRegistryObject<T>> registerGemSetItem(Function<Gems, String> nameFactory, Function<Gems, T> itemFactory) {
        Map<Gems, ItemRegistryObject<T>> map = new EnumMap<>(Gems.class);
        for (Gems gem : Gems.values()) {
            map.put(gem, register(nameFactory.apply(gem), () -> itemFactory.apply(gem)));
        }
        return ImmutableMap.copyOf(map);
    }

    private static Item.Properties baseProps() {
        return new Item.Properties().tab(ChaosMod.ITEM_GROUP);
    }

    private static Item.Properties unstackableProps() {
        return baseProps().stacksTo(1);
    }
}
