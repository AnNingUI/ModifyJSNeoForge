package com.anningui.modifyjs.event.register;


import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.HashSet;
import java.util.Set;

import static com.anningui.modifyjs.event.MJSRenderEvents.REGISTER_ADDER_REGISTER;

public class ModelAdditionalRegister implements KubeStartupEvent {
    @HideFromJS
    public static Set<ModelResourceLocation> modelsAdderSet = new HashSet<>();
    @HideFromJS
    public ModelAdditionalRegister() {

    }
    @HideFromJS
    public static void onEvent(ModelEvent.RegisterAdditional event) {
        REGISTER_ADDER_REGISTER.post(new ModelAdditionalRegister());
        modelsAdderSet.forEach(event::register);
    }
    public void register(String modelNamespace,String modelPath) {
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath(modelNamespace, modelPath);
        modelsAdderSet.add(ModelResourceLocation.standalone(model));
    }

    public void register(String modelPath) {
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath(KubeJS.MOD_ID, modelPath);
        modelsAdderSet.add(ModelResourceLocation.standalone(model));
    }
}
