package com.anningui.modifyjs.mod_adder.mek.custom.item;


import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.MekanismAPI;
import mekanism.api.providers.IModuleDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class KubeJSUnitItemBuilder extends ItemBuilder {
    public transient KubeJSModuleDataBuilder moduleDataBuilder;
    private IModuleDataProvider<?> moduleData;
    public KubeJSUnitItemBuilder(ResourceLocation id) {
        super(id);
        this.moduleDataBuilder = new KubeJSModuleDataBuilder(id);
    }

    public KubeJSUnitItemBuilder setModuleData(Consumer<KubeJSModuleDataBuilder> moduleBuilder) {
        moduleBuilder.accept(moduleDataBuilder);
        return this;
    }

    @Override
    public KubeJSUnitItem createObject() {
        return new KubeJSUnitItem(getModuleData(), this);
    }

    @HideFromJS
    public IModuleDataProvider<?> getModuleData() {
        return this.moduleData;
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        if (!isNull(moduleDataBuilder)) {
            registry.add(MekanismAPI.MODULE_REGISTRY_NAME, moduleDataBuilder);
        }
    }
}