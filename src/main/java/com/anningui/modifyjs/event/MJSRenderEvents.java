package com.anningui.modifyjs.event;

import com.anningui.modifyjs.event.register.ArmorLayerRegister;
import com.anningui.modifyjs.event.register.ItemRenderRegister;
import com.anningui.modifyjs.event.register.ModelAdditionalRegister;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface MJSRenderEvents {
    EventGroup GROUP = EventGroup.of("MJSRenderEvents");
    EventHandler ITEM_RENDER_REGISTER = GROUP.startup("itemRenderRegister", () -> ItemRenderRegister.class);
    EventHandler REGISTER_ADDER_REGISTER = GROUP.startup("modelAdderRegister", () -> ModelAdditionalRegister.class);
    EventHandler ARMOR_LAYER_REGISTER = GROUP.startup("armorLayerRegister", () -> ArmorLayerRegister.class);
}
