MJSRenderEvents.modelAdderRegister(e => {
    e.register("kubejs", "adder/adder")
})
/*
let bkM = MJSRenderUtils.getModel($ResourceLocation.parse("kubejs:adder/adder"))
MJSRenderUtils.renderModelLists(
    bkM,
    Item.of("minecraft:stone"),
    poseStack,
    buffer,
    packedLight,
    $OverlayTexture.NO_OVERLAY
)
*/