package com.anningui.modifyjs.mixin;

import com.anningui.modifyjs.callback.MixinStron;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

import static com.anningui.modifyjs.callback.MixinStron.noShowModelMap;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Unique private Set<ArmorItem> mjs$armorItemSet;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(RenderLayerParent<T, M> renderer, A innerModel, A outerModel, ModelManager modelManager, CallbackInfo ci) {
        MixinStron.innerModel = (HumanoidModel<LivingEntity>) innerModel;
        MixinStron.outerModel = (HumanoidModel<LivingEntity>) outerModel;
        mjs$armorItemSet = new HashSet<>();
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void getItem(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!livingEntity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()){
            mjs$armorItemSet.add((ArmorItem) livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem());
        }

        if (!livingEntity.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            mjs$armorItemSet.add((ArmorItem) livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem());
        }

        if (!livingEntity.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
            mjs$armorItemSet.add((ArmorItem) livingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem());
        }

        if (!livingEntity.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
            mjs$armorItemSet.add((ArmorItem) livingEntity.getItemBySlot(EquipmentSlot.FEET).getItem());
        }
    }

    @Inject(method = "setPartVisibility", at = @At(value = "TAIL"))
    private void setPartVisibility(A model, EquipmentSlot slot, CallbackInfo ci) {
        noShowModelMap.forEach((item, isNoShow) -> {
            if (item instanceof ArmorItem armorItem) {
                A thisModel = getArmorModel(slot);
                A armorModel = getArmorModel(armorItem.getEquipmentSlot());
                if (armorModel == thisModel && armorItem.getEquipmentSlot() == slot && mjs$armorItemSet.contains(armorItem) && isNoShow) {
                    switch (slot) {
                        case HEAD:
                            model.head.visible = false;
                            model.hat.visible = false;
                            break;
                        case CHEST:
                            model.body.visible = false;
                            model.rightArm.visible = false;
                            model.leftArm.visible = false;
                            break;
                        case LEGS:
                            model.body.visible = false;
                            model.rightLeg.visible = false;
                            model.leftLeg.visible = false;
                            break;
                        case FEET:
                            model.rightLeg.visible = false;
                            model.leftLeg.visible = false;
                    }
                }
            }
        });
    }

    @Shadow
    public abstract A getArmorModel(EquipmentSlot slot);
}


