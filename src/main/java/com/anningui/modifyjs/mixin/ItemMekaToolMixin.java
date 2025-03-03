package com.anningui.modifyjs.mixin;

import mekanism.api.gear.IModule;
import mekanism.common.content.gear.*;
import mekanism.common.content.gear.Module;
import mekanism.common.item.ItemEnergized;
import mekanism.common.lib.radial.IGenericRadialModeItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(mekanism.common.item.gear.ItemMekaTool.class)
public abstract class ItemMekaToolMixin extends ItemEnergized implements IModuleContainerItem, IBlastingItem, IGenericRadialModeItem {

    public ItemMekaToolMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player) {
            ModuleContainer container = ModuleHelper.get().getModuleContainer(pStack);
            if (container!= null) {
                for (Module<?> module : container.modules()) {
                    module.tick(container, pStack, (Player) pEntity);
                }
            }
        }
    }
}