package com.anningui.modifyjs.render.layer;

import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.anningui.modifyjs.event.register.ArmorLayerRegister.allArmorLayersToEntity;

@OnlyIn(Dist.CLIENT)
public class MJSRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {


    public MJSRenderLayer(LivingEntityRenderer<T,M> renderer) {
        super(renderer);
    }


    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        allArmorLayersToEntity.forEach((entityType, consumer) -> {
            if (entityType.equals(livingEntity.getType())) {
                consumer.accept(new ArmorLayerContext(poseStack, bufferSource, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch));
            }
        });
    }
}
