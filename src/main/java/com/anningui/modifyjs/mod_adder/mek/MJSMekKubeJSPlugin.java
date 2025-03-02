package com.anningui.modifyjs.mod_adder.mek;

import com.anningui.modifyjs.mod_adder.mek.custom.item.KubeJSUnitItemBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.KubeJSModuleUtils;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.config.ModuleEnumConfig;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.util.StorageUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import static com.anningui.modifyjs.ModifyJS.mekAdderIsOk;
import static com.anningui.modifyjs.ModifyJS.mjs$id;

public class MJSMekKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        if (mekAdderIsOk) {
            registry.of(Registries.ITEM, res -> {
               res.add(mjs$id("mek_unit"), KubeJSUnitItemBuilder.class, KubeJSUnitItemBuilder::new);
            });
        }
    }

    @Override
    public void registerBindings(BindingRegistry event) {
        if (mekAdderIsOk) {
            event.add("MekModuleUtils", KubeJSModuleUtils.class);
            // 兼容旧版本使用相同类但不同保留旧版命名
            event.add("KJSModuleUtils", KubeJSModuleUtils.class);
            event.add("MekIModuleHelper", IModuleHelper.class);
            event.add("MekModuleHelper", ModuleHelper.class);
            event.add("MekStorageUtils", StorageUtils.class);
            event.add("ModuleEnumConfig", ModuleEnumConfig.class);
        }
    }
}
