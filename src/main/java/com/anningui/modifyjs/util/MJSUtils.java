package com.anningui.modifyjs.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MJSUtils {
    public static Item getItemById(ResourceLocation id) {
        return BuiltInRegistries.ITEM.get(id);
    }
    public static ResourceLocation getItemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }
    public static ResourceLocation getItemIdByItemStack(ItemStack item) {
        return BuiltInRegistries.ITEM.getKey(item.getItem());
    }
}
