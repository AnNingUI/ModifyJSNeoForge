package com.anningui.modifyjs;

import mekanism.common.integration.MekanismHooks;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod(ModifyJS.MOD_ID)
public class ModifyJS
{
    public static final String MOD_ID = "modifyjs";
    public static boolean mekAdderIsOk = ModList.get().isLoaded("mekanism");
    public static ResourceLocation mjs$id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
    private static final Logger LOGGER = LogUtils.getLogger();


    public static Object hooks;

    public ModifyJS() {

        LOGGER.info("ModifyJS mod initializing");
        if (mekAdderIsOk) {
            hooks = new MekanismHooks();
        }
    }
}
