package com.anningui.modifyjs.render.layer;

import com.anningui.modifyjs.callback.ArmorLayerContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.anningui.modifyjs.event.register.ArmorLayerRegister.allArmorLayers;

@OnlyIn(Dist.CLIENT)
public class MJSHorseArmorLayer extends HorseArmorLayer {
    public MJSHorseArmorLayer(RenderLayerParent<Horse, HorseModel<Horse>> renderer, EntityModelSet modelSet) {
        super(renderer, modelSet);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull Horse livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        allArmorLayers.forEach((k, v) -> {
            ArmorItem item = (ArmorItem) k;
            EquipmentSlot slot = item.getEquipmentSlot();
            if (item instanceof AnimalArmorItem animalarmoritem && livingEntity.getItemBySlot(slot).getItem() == item && animalarmoritem.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN) {
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
    }
}
