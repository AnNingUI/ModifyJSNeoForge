package com.anningui.modifyjs;

import com.anningui.modifyjs.event.register.ArmorLayerRegister;
import com.anningui.modifyjs.event.register.ItemRenderRegister;
import com.anningui.modifyjs.event.register.ModelAdditionalRegister;
import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import com.anningui.modifyjs.render.item.MJSBakeModel;
import com.anningui.modifyjs.util.MJSUtils;
import mekanism.api.MekanismIMC;
import mekanism.common.integration.MekanismHooks;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.Map;
import java.util.Set;

import static com.anningui.modifyjs.ModifyJS.*;
import static com.anningui.modifyjs.event.register.ItemRenderRegister.itemRendererMap;
import static com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder.getAllModuleDataBuilder;
import static com.anningui.modifyjs.mod_adder.mek.util.KubeJSMekUntiItemUtils.getModuleById;
import static java.util.Objects.isNull;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModifyJSClient {
    @SubscribeEvent
    public static void onModelRegistryAdder(ModelEvent.RegisterAdditional event) {
        ModelAdditionalRegister.onEvent(event);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        ItemRenderRegister.onEvent(event);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        ArmorLayerRegister.onEvent(event);
    }

    @SubscribeEvent
    public static void onModelBaked(ModelEvent.ModifyBakingResult event){
        // wrench item model
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();
        var map2set = itemRendererMap.entrySet();
        map2set.forEach(entry -> {
            var item = entry.getKey();
            var rendererCtx = entry.getValue();
            if (isNull(rendererCtx)) return;
            ModelResourceLocation location = new ModelResourceLocation(
                    MJSUtils.getItemId(item), "inventory"
            );
            BakedModel existingModel = modelRegistry.get(location);
            if (!(existingModel == null || existingModel instanceof MJSBakeModel)) {
                MJSBakeModel obsidianWrenchBakedModel = new MJSBakeModel(existingModel);
                event.getModels().put(location, obsidianWrenchBakedModel);
            }
        });
    }

    @SubscribeEvent
    public static void onImcQueue(InterModEnqueueEvent event) {
        if (mekAdderIsOk && !isNull(hooks)) {

            ((MekanismHooks) hooks).sendIMCMessages(event);
            Set<KubeJSModuleDataBuilder> a = getAllModuleDataBuilder();
            a.forEach(b -> {
                UnitItemSlots.Slots s  = b.slot;
                ResourceLocation id = b.id;
                var m  = getModuleById(id);
                switch (s) {
                    case ALL -> MekanismIMC.addModulesToAll(m);
                    case MEK_TOOL -> MekanismIMC.addMekaToolModules(m);
                    case MEK_SUIT_HELMET -> MekanismIMC.addMekaSuitHelmetModules(m);
                    case MEK_SUIT_BODY -> MekanismIMC.addMekaSuitBodyarmorModules(m);
                    case MEK_SUIT_PANTS -> MekanismIMC.addMekaSuitPantsModules(m);
                    case MEK_SUIT_BOOTS -> MekanismIMC.addMekaSuitBootsModules(m);
                }
            });
        }
    }
}
