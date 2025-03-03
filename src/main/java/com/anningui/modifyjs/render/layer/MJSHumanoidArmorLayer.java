package com.anningui.modifyjs.render.layer;

import com.anningui.modifyjs.ModifyJS;
import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.anningui.modifyjs.event.register.ArmorLayerRegister.allArmorLayers;
import static com.anningui.modifyjs.util.MJSUtils.getItemId;

@OnlyIn(Dist.CLIENT)
public class MJSHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {
    public MJSHumanoidArmorLayer(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager) {
        super(renderer, innerModel, outerModel, modelManager);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        allArmorLayers.forEach((k, v) -> {
            if (shouldRenderArmor(livingEntity, getItemId(k))) {
                v.accept(new ArmorLayerContext(
                        poseStack,
                        buffer,
                        packedLight,
                        livingEntity,
                        limbSwing,
                        limbSwingAmount,
                        partialTicks,
                        ageInTicks,
                        netHeadYaw,
                        headPitch
                ));
            }
        });
        super.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    private boolean shouldRenderArmor(T livingEntity, ResourceLocation armorId) {
        List<ResourceLocation> ids = List.of(
                getItemId(livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem()),
                getItemId(livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem()),
                getItemId(livingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem()),
                getItemId(livingEntity.getItemBySlot(EquipmentSlot.FEET).getItem()),
                getItemId(livingEntity.getItemBySlot(EquipmentSlot.BODY).getItem())
        );
        int index = ids.indexOf(armorId);
        return index != -1;
    }
}
