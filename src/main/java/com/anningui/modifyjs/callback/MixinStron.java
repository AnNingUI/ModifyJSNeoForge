package com.anningui.modifyjs.callback;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixinStron {
    public static List<RenderLayer<?, ?>> layers = new ArrayList<>();
    public static HumanoidModel<LivingEntity> innerModel = null;
    public static HumanoidModel<LivingEntity> outerModel = null;

    public static Map<ArmorItem, Boolean> noShowModelMap = new HashMap<>();

    public static boolean isInitialized() {
        return innerModel != null && outerModel != null && !layers.isEmpty();
    }
}
