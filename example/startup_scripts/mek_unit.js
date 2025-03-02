StartupEvents.registry("item", event => {
    event.create("example_item", "modifyjs:mek_unit").setModuleData(
        (builder) => {
            builder.slot("all")
            builder.maxStackSize(1)
            builder.onAdded((ik, ic, itemStack) => {
                Client.player.tell(itemStack)
            })
        }
    ).maxStackSize(16)
})