const $OverlayTexture = Java.loadClass("net.minecraft.client.renderer.texture.OverlayTexture")
const $ItemDisplayContext = Java.loadClass("net.minecraft.world.item.ItemDisplayContext")
const $ResourceLocation = Java.loadClass("net.minecraft.resources.ResourceLocation")
const $Axis = Java.loadClass("com.mojang.math.Axis");


StartupEvents.registry("item", event => {
    event.create("example_horse_armor", "kubejs:animal_armor")
        .bodyType("equestrian").overlay(true)
})


MJSRenderEvents.itemRenderRegister(e => {
    e.register("kubejs:example_horse_armor", global.testRender)
})

MJSRenderEvents.armorLayerRegister(e => {
    e.register("kubejs:example_horse_armor", global.testArmorLayer)
})

/**
 *
 * @param {Item} itemStack
 * @param {$ItemDisplayContext_} itemDisplayCtx
 * @param {$PoseStack_} poseStack
 * @param {$MultiBufferSource_} buffer
 * @param {number} packedLight
 * @param {number} packedOverlay
 */
global.testRender = (itemStack, itemDisplayCtx, poseStack, buffer, packedLight, packedOverlay) => {
    let itemRenderer = Client.getItemRenderer()
    poseStack.pushPose()
    poseStack.translate(0.5, 0.5, 0.5)
    poseStack.mulPose($Axis.YP.rotationDegrees(Date.now() % 3600));
    itemRenderer.renderStatic(
        Item.of("minecraft:stone"),
        itemDisplayCtx,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        Client.level,
        0
    )
    poseStack.popPose()
}


/**
 *
 * @param {$ArmorLayerContext_} ctx
 */
global.testArmorLayer = (ctx) => {
    let { poseStack, buffer, packedLight } = ctx
    let itemRenderer = Client.getItemRenderer()
    poseStack.pushPose()
    poseStack.translate(0.5, 0.5, 0.5)
    poseStack.mulPose($Axis.YP.rotationDegrees(Date.now() % 3600));
    itemRenderer.renderStatic(
        Item.of("minecraft:stone"),
        $ItemDisplayContext.GROUND,
        packedLight,
        $OverlayTexture.NO_OVERLAY,
        poseStack,
        buffer,
        Client.level,
        0
    )
    poseStack.popPose()
}