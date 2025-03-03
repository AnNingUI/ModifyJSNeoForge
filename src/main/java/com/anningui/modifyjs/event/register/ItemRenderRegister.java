package com.anningui.modifyjs.event.register;

import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.render.item.KJSClientItemExtensions;
import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.HashMap;
import java.util.Map;

import static com.anningui.modifyjs.event.MJSRenderEvents.ITEM_RENDER_REGISTER;
import static java.util.Objects.isNull;

public class ItemRenderRegister implements KubeStartupEvent {
    @HideFromJS
    public static Map<Item, CustomInterface.RenderByItemCallback> itemRendererMap = new HashMap<>();
    @HideFromJS
    public ItemRenderRegister() {

    }
    @HideFromJS
    public static void onEvent(RegisterClientExtensionsEvent event) {
        ITEM_RENDER_REGISTER.post(new ItemRenderRegister());
        if (!itemRendererMap.isEmpty()) {
            itemRendererMap.forEach((k,v)->{
                event.registerItem(new KJSClientItemExtensions(v), k);
            });
        }
    }

    public void register(Item in, CustomInterface.RenderByItemCallback renderCallback) {
        itemRendererMap.put(in, renderCallback);
    }
}
