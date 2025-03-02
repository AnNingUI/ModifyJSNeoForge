package com.anningui.modifyjs;

import com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import dev.architectury.platform.Platform;
import mekanism.api.MekanismIMC;
import mekanism.common.integration.MekanismHooks;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.Set;

import static com.anningui.modifyjs.ModifyJS.hooks;
import static com.anningui.modifyjs.ModifyJS.mekAdderIsOk;
import static com.anningui.modifyjs.mod_adder.mek.custom.module.KubeJSModuleDataBuilder.getAllModuleDataBuilder;
import static com.anningui.modifyjs.mod_adder.mek.util.KubeJSMekUntiItemUtils.getModuleById;
import static java.util.Objects.isNull;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModifyJSClient {
    @SubscribeEvent
    public static void onImcQueue(InterModEnqueueEvent event) {
        if (mekAdderIsOk && !isNull(hooks)) {

            ((MekanismHooks) hooks).sendIMCMessages(event);
            Set<KubeJSModuleDataBuilder> a = getAllModuleDataBuilder();
            a.forEach(b -> {
                UnitItemSlots.Slots s  = b.slot;
                ResourceLocation id = b.id;
                var m  = getModuleById(id);
                switch (s) {
                    case ALL -> MekanismIMC.addModulesToAll(m);
                    case MEK_TOOL -> MekanismIMC.addMekaToolModules(m);
                    case MEK_SUIT_HELMET -> MekanismIMC.addMekaSuitHelmetModules(m);
                    case MEK_SUIT_BODY -> MekanismIMC.addMekaSuitBodyarmorModules(m);
                    case MEK_SUIT_PANTS -> MekanismIMC.addMekaSuitPantsModules(m);
                    case MEK_SUIT_BOOTS -> MekanismIMC.addMekaSuitBootsModules(m);
                }
            });
        }
    }
}
