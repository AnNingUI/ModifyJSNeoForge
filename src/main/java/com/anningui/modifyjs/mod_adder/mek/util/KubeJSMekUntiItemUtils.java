package com.anningui.modifyjs.mod_adder.mek.util;

import mekanism.api.MekanismAPI;
import mekanism.api.gear.ModuleData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.List;

public class KubeJSMekUntiItemUtils {
    public static ModuleData<?> getModuleById(ResourceLocation id) {
        return MekanismAPI.MODULE_REGISTRY.get(id);
    }
//
    public static Item getItemById(ResourceLocation id) {
        return BuiltInRegistries.ITEM.get(id);
    }
//
    public static List<ModuleData<?>> getAllModule() {
        return MekanismAPI.MODULE_REGISTRY.stream().toList();
    }


}
