package com.anningui.modifyjs.kubejs;

import com.anningui.modifyjs.util.js_long.SwitchMap;
import com.anningui.modifyjs.util.js_long.TryCatchPipe;
import com.anningui.modifyjs.util.render.MJSRenderUtils;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.InteractionResult;

public class MJSKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("TryCatchPipe", TryCatchPipe.class);
        bindings.add("SwitchMap", SwitchMap.class);
        bindings.add("MJSRenderUtils", MJSRenderUtils.class);
        bindings.add("ModelResourceLocation", ModelResourceLocation.class);
        bindings.add("InteractionResult", InteractionResult.class);
    }
}
