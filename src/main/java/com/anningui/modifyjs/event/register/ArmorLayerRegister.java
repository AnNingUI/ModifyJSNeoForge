package com.anningui.modifyjs.event.register;

import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.anningui.modifyjs.callback.MixinStron;
import com.anningui.modifyjs.render.layer.MJSHorseArmorLayer;
import com.anningui.modifyjs.render.layer.MJSHumanoidArmorLayer;
import com.anningui.modifyjs.render.layer.MJSRenderLayer;
import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.anningui.modifyjs.callback.MixinStron.noShowModelMap;
import static com.anningui.modifyjs.event.MJSRenderEvents.ARMOR_LAYER_REGISTER;
import static java.util.Objects.isNull;

public class ArmorLayerRegister implements KubeStartupEvent {
    @HideFromJS
    public static Map<Item, Consumer<ArmorLayerContext>> allArmorLayers = new HashMap<>();

    @HideFromJS
    public static Map<EntityType<?>, Consumer<ArmorLayerContext>> allArmorLayersToEntity = new HashMap<>();

    @HideFromJS
    public ArmorLayerRegister() {
    }
    @HideFromJS
    public static void onEvent(EntityRenderersEvent.AddLayers event) {
        ARMOR_LAYER_REGISTER.post(new ArmorLayerRegister());
        for (PlayerSkin.Model skinName : event.getSkins()) {
            addCustomLayers(event, EntityType.PLAYER ,(PlayerRenderer) event.getSkin(skinName), event.getContext().getModelManager());
        }
        //Add our own custom armor layer to everything that has an armor layer
        //Note: This includes any modded mobs that have vanilla's BipedArmorLayer added to them
        for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
            EntityRenderer<?> renderer = entry.getValue();
            if (renderer instanceof LivingEntityRenderer) {
                EntityType<?> entityType =  entry.getKey();
                //noinspection unchecked,rawtypes
                addCustomLayers(
                        event, entityType,
                        (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) event.getRenderer((EntityType) entityType),
                        event.getContext().getModelManager()
                );
            }
        }
    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>> void addCustomLayers(EntityRenderersEvent.AddLayers event, EntityType<?> entityType,@Nullable LivingEntityRenderer<T, M> renderer, ModelManager modelManager) {
        if (renderer == null || !MixinStron.isInitialized()
        ) {
            return;
        }
        HumanoidArmorLayer<T, M, ?> bipedArmorLayer = null;
        for (RenderLayer<T, M> layerRenderer : (List<RenderLayer<T, M>>) (Object) MixinStron.layers) {
            //Validate against the layer render being null, as it seems like some mods do stupid things and add in null layers
            if (layerRenderer != null) {
                //Only allow an exact class match, so we don't add to modded entities that only have a modded extended armor or elytra layer
                Class<?> layerClass = layerRenderer.getClass();
                if (layerClass == HumanoidArmorLayer.class) {
                    bipedArmorLayer = (HumanoidArmorLayer<T, M, ?>) layerRenderer;
                }
                if (!isNull(bipedArmorLayer)) {
                    break;
                }
            }
        }
        if (bipedArmorLayer != null) {
            if (entityType == EntityType.HORSE) {
                var horseRenderer = (HorseRenderer) (Object) renderer;
                horseRenderer.addLayer(
                        new MJSHorseArmorLayer(
                                horseRenderer,
                                event.getEntityModels()
                        )
                );
            } else if (entityType == EntityType.PLAYER) {
                renderer.addLayer(
                        new MJSHumanoidArmorLayer<>(
                                renderer,
                                (HumanoidModel<T>) MixinStron.innerModel,
                                (HumanoidModel<T>) MixinStron.outerModel,
                                modelManager
                        )
                );
            } else {
                renderer.addLayer(
                        new MJSRenderLayer<>(
                                renderer
                        )
                );
            }
        }
    }

    @HideFromJS
    public void register(Item in, Consumer<ArmorLayerContext> consumer, boolean noModel) {
        if (in instanceof ArmorItem) {
            noShowModelMap.put((ArmorItem) in, noModel);
            allArmorLayers.put(in, consumer);
        }
    }

    @Info("Only Player and Horse")
    public void register(Item in, Consumer<ArmorLayerContext> consumer) {
        register(in, consumer, false);
    }

    @Info("No original model render, but only HumanModel And HorseModel")
    public void registerNoModel(Item in, Consumer<ArmorLayerContext> consumer) {
        register(in, consumer, true);
    }

    @Info("Register a custom renderLayer for a specific entity type.")
    public void registerCustom(EntityType<?> in, Consumer<ArmorLayerContext> consumer) {
        allArmorLayersToEntity.put(in, consumer);
    }
}
