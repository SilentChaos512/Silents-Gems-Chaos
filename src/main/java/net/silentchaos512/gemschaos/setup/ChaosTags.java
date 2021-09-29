package net.silentchaos512.gemschaos.setup;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.gemschaos.ChaosMod;

public class ChaosTags {
    public static final class Blocks {
        private Blocks() {}

        private static Tag.Named<Block> forge(String path) {
            return tag("forge", path);
        }

        private static Tag.Named<Block> gems(String path) {
            return tag(ChaosMod.MOD_ID, path);
        }

        private static Tag.Named<Block> tag(String namespace, String name) {
            return BlockTags.bind(new ResourceLocation(namespace, name).toString());
        }
    }

    public static final class Items {
        public static final Tag.Named<Item> DIRT = forge("dirt");
        public static final Tag.Named<Item> GEMS_CHAOS = mod("gems/chaos");

        private Items() {}

        private static Tag.Named<Item> forge(String path) {
            return tag("forge", path);
        }

        private static Tag.Named<Item> mod(String path) {
            return tag(ChaosMod.MOD_ID, path);
        }

        private static Tag.Named<Item> tag(String namespace, String name) {
            return ItemTags.bind(new ResourceLocation(namespace, name).toString());
        }
    }
}
