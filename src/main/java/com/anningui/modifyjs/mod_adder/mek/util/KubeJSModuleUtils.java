package com.anningui.modifyjs.mod_adder.mek.util;


import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleData;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.content.gear.shared.ModuleEnergyUnit;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.item.gear.ItemMekaTool;
import mekanism.common.registries.MekanismModules;
import mekanism.common.util.StorageUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class KubeJSModuleUtils {
    public static <MODULE extends ICustomModule<MODULE>> @NotNull IModule<MODULE> getModuleById(ItemStack stack, ResourceLocation id) {
        return (IModule<MODULE>) ModuleHelper.INSTANCE.getModule(stack, KubeJSMekUntiItemUtils.getModuleById(id));
    }

    public static @Nullable <MODULE extends ICustomModule<MODULE>> IModule<MODULE> getModuleByClassType(ItemStack stack, ResourceLocation id, Class<MODULE> moduleClass) {
        var module = getModuleById(stack, id);
        if (!moduleClass.isInstance(module.getCustomInstance())) {
            return null;
        }
        return (IModule<MODULE>) module;
    }
//
    public static IModule<KubeJSModuleData> getModuleByKjs(ItemStack stack, ResourceLocation id) {
        return getModuleByClassType(stack, id, KubeJSModuleData.class);
    }
//
    public static long getMaxEnergy(ItemStack stack) {
        IEnergyContainer energyContainer = StorageUtils.getEnergyContainer(stack, 0);
        if (energyContainer != null) {
            return energyContainer.getMaxEnergy();
        } else {
            return 0;
        }
    }


}
