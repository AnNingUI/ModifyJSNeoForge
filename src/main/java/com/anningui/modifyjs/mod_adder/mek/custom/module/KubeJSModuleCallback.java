package com.anningui.modifyjs.mod_adder.mek.custom.module;


import com.anningui.modifyjs.callback.CustomInterface;
import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleContainer;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.IRadialMode;
import mekanism.api.radial.mode.NestedRadialMode;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@HideFromJS
public class KubeJSModuleCallback {
    public CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Void> tickServerCallback;
    
    public CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Void> tickClientCallback;
    
    public CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Consumer<Component>, Void> addHUDStringsCallback;
    
    public CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Consumer<IHUDElement>, Void> addHUDElementsCallback;
    
    public Function<IModule<KubeJSModuleData>, Boolean> canChangeModeWhenDisabledCallback;
    
    public Function<IModule<KubeJSModuleData>, Boolean> canChangeRadialModeWhenDisabledCallback;
    
    public BiFunction<IModule<KubeJSModuleData>, ItemStack, Component> getModeScrollComponentCallback;
    
    public CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, IModuleContainer, ItemStack, Integer, Boolean, Void> changeModeCallback;
    
    public CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, Consumer<NestedRadialMode>, Void> addRadialModesCallback;
    
    public CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode> getModeCallback;
    
    public CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, IModuleContainer, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode, Boolean> setModeCallback;
    
    public CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Boolean, Void> onAddedCallback;
    public CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Boolean, Void> onRemovedCallback;
    
    public BiFunction<IModule<KubeJSModuleData>, DamageSource, ICustomModule.ModuleDamageAbsorbInfo> getDamageAbsorbInfoCallback;
    
    public BiFunction<IModule<KubeJSModuleData>, UseOnContext, InteractionResult> onItemUseCallback;
    
    public CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, ItemAbility, Boolean> canPerformActionCallback;
    
    public CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, LivingEntity, InteractionHand, IModuleContainer, ItemStack, InteractionResult> onInteractCallback;
    
    public CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, BlockSource, ICustomModule.ModuleDispenseResult> onDispenseCallback;
}
