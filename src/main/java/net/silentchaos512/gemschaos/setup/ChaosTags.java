package net.silentchaos512.gemschaos.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gemschaos.ChaosMod;

public class ChaosTags {
    public static final class Blocks {
        private Blocks() {}

        private static TagKey<Block> forge(String path) {
            return tag("forge", path);
        }

        private static TagKey<Block> gems(String path) {
            return tag(ChaosMod.MOD_ID, path);
        }

        private static TagKey<Block> tag(String namespace, String name) {
            return BlockTags.create(new ResourceLocation(namespace, name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> DIRT = forge("dirt");
        public static final TagKey<Item> GEMS_CHAOS = mod("gems/chaos");

        private Items() {}

        private static TagKey<Item> forge(String path) {
            return tag("forge", path);
        }

        private static TagKey<Item> mod(String path) {
            return tag(ChaosMod.MOD_ID, path);
        }

        private static TagKey<Item> tag(String namespace, String name) {
            return ItemTags.create(new ResourceLocation(namespace, name));
        }
    }
}
