package com.anningui.modifyjs.mod_adder.mek.custom.module;

import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.util.js_long.TryCatchPipe;
import com.mojang.logging.LogUtils;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;

@ParametersAreNotNullByDefault
public class KubeJSModuleData implements ICustomModule<KubeJSModuleData> {
    public KubeJSModuleDataBuilder builder;
    public IModule<KubeJSModuleData> im;
    public KubeJSModuleCallback callback;
    private static final Logger LOGGER = LogUtils.getLogger();

    public KubeJSModuleData(IModule<KubeJSModuleData> im) {
        this.im = im;
        this.builder = getBuilder();
        this.callback = this.builder.callback;
    }

    public KubeJSModuleDataBuilder getBuilder() {
        return null;
    }

    @Override
    public void tickServer(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull Player player) {
        tryCatchBBV(
                !isNull(callback.tickServerCallback),
                () -> callback.tickServerCallback.apply(module, moduleContainer, stack, player),
                () -> ICustomModule.super.tickServer(module, moduleContainer, stack, player)
        );
    }

    @Override
    public void tickClient(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull Player player) {
        tryCatchBBV(
                !isNull(callback.tickClientCallback),
                () -> callback.tickClientCallback.apply(module, moduleContainer, stack, player),
                () -> ICustomModule.super.tickClient(module, moduleContainer, stack, player)
        );
    }

    @Override
    public void addHUDStrings(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull Player player, @NotNull Consumer<Component> hudStringAdder) {
        tryCatchBBV(
                !isNull(callback.addHUDStringsCallback),
                () -> callback.addHUDStringsCallback.apply(module, moduleContainer, stack, player, hudStringAdder),
                () -> ICustomModule.super.addHUDStrings(module, moduleContainer, stack, player, hudStringAdder)
        );
    }

    @Override
    public void addHUDElements(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull Player player, @NotNull Consumer<IHUDElement> hudElementAdder) {
        tryCatchBBV(
                !isNull(callback.addHUDElementsCallback),
                () -> callback.addHUDElementsCallback.apply(module, moduleContainer, stack, player, hudElementAdder),
                () -> ICustomModule.super.addHUDElements(module, moduleContainer, stack, player, hudElementAdder)
        );
    }

    @Override
    public boolean canChangeModeWhenDisabled(@NotNull IModule<KubeJSModuleData> module) {
        return tryCatchBBR(
                !isNull(callback.canChangeModeWhenDisabledCallback),
                (v) -> callback.canChangeModeWhenDisabledCallback.apply(module),
                (e) -> ICustomModule.super.canChangeModeWhenDisabled(module)
        );
    }

    @Override
    public boolean canChangeRadialModeWhenDisabled(@NotNull IModule<KubeJSModuleData> module) {
        return tryCatchBBR(
                !isNull(callback.canChangeRadialModeWhenDisabledCallback),
                (v) -> callback.canChangeRadialModeWhenDisabledCallback.apply(module),
                (e) -> ICustomModule.super.canChangeRadialModeWhenDisabled(module)
        );
    }

    @Override
    public @Nullable Component getModeScrollComponent(@NotNull IModule<KubeJSModuleData> module, @NotNull ItemStack stack) {
        return tryCatchBBR(
                !isNull(callback.getModeScrollComponentCallback),
                (v) -> callback.getModeScrollComponentCallback.apply(module, stack),
                (e) -> ICustomModule.super.getModeScrollComponent(module, stack)
        );
    }

    @Override
    public void changeMode(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, int shift, boolean displayChangeMessage) {
        tryCatchBBV(
                !isNull(callback.changeModeCallback),
                () -> callback.changeModeCallback.apply(module, player, moduleContainer, stack, shift, displayChangeMessage),
                () -> ICustomModule.super.changeMode(module, player, moduleContainer, stack, shift, displayChangeMessage)
        );
    }

    @Override
    public void addRadialModes(@NotNull IModule<KubeJSModuleData> module, @NotNull ItemStack stack, @NotNull Consumer<NestedRadialMode> adder) {
        tryCatchBBV(
                !isNull(callback.addRadialModesCallback),
                () -> callback.addRadialModesCallback.apply(module, stack, adder),
                () -> ICustomModule.super.addRadialModes(module, stack, adder)
        );
    }

    @Override
    public <MODE extends IRadialMode> @Nullable MODE getMode(@NotNull IModule<KubeJSModuleData> module, @NotNull ItemStack stack, @NotNull RadialData<MODE> radialData) {
        return tryCatchBBR(
                !isNull(callback.getModeCallback),
                (v) -> (MODE) callback.getModeCallback.apply(module, stack, radialData),
                (e) -> ICustomModule.super.getMode(module, stack, radialData)
        );
    }

    @Override
    public <MODE extends IRadialMode> boolean setMode(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull RadialData<MODE> radialData, @NotNull MODE mode) {
        return tryCatchBBR(
                !isNull(callback.setModeCallback),
                (v) -> {
                    CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, IModuleContainer, ItemStack, RadialData<MODE>, MODE, Boolean> setModeCallback = (CustomInterface.KSextConsumer) callback.setModeCallback;
                    return setModeCallback.apply(module, player, moduleContainer, stack, radialData, mode);
                },
                (e) -> ICustomModule.super.setMode(module, player, moduleContainer, stack, radialData, mode)
        );
    }

    @Override
    public void onAdded(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, boolean first) {
        tryCatchBBV(
                !isNull(callback.onAddedCallback),
                () -> callback.onAddedCallback.apply(module, moduleContainer, stack, first),
                () -> ICustomModule.super.onAdded(module, moduleContainer, stack, first)
        );
    }

    @Override
    public void onRemoved(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, boolean wasLast) {
        tryCatchBBV(
                !isNull(callback.onRemovedCallback),
                () -> callback.onRemovedCallback.apply(module, moduleContainer, stack, wasLast),
                () -> ICustomModule.super.onRemoved(module, moduleContainer, stack, wasLast)
        );
    }

    @Override
    public @Nullable ModuleDamageAbsorbInfo getDamageAbsorbInfo(@NotNull IModule<KubeJSModuleData> module, @NotNull DamageSource damageSource) {
        return tryCatchBBR(
                !isNull(callback.getDamageAbsorbInfoCallback),
                (v) -> callback.getDamageAbsorbInfoCallback.apply(module, damageSource),
                (e) -> ICustomModule.super.getDamageAbsorbInfo(module, damageSource)
        );
    }

    @Override
    public @NotNull InteractionResult onItemUse(@NotNull IModule<KubeJSModuleData> module, @NotNull UseOnContext context) {
        return tryCatchBBR(
                !isNull(callback.onItemUseCallback),
                (v) -> callback.onItemUseCallback.apply(module, context),
                (e) -> ICustomModule.super.onItemUse(module, context)
        );
    }

    @Override
    public boolean canPerformAction(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull ItemAbility ability) {
        return tryCatchBBR(
                !isNull(callback.canPerformActionCallback),
                (v) -> callback.canPerformActionCallback.apply(module, moduleContainer, stack, ability),
                (e) -> ICustomModule.super.canPerformAction(module, moduleContainer, stack, ability)
        );
    }

    @Override
    public @NotNull InteractionResult onInteract(@NotNull IModule<KubeJSModuleData> module, @NotNull Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack) {
        return tryCatchBBR(
                !isNull(callback.onInteractCallback),
                (v) -> callback.onInteractCallback.apply(module, player, entity, hand, moduleContainer, stack),
                (e) -> ICustomModule.super.onInteract(module, player, entity, hand, moduleContainer, stack)
        );
    }

    @Override
    public @NotNull ModuleDispenseResult onDispense(@NotNull IModule<KubeJSModuleData> module, @NotNull IModuleContainer moduleContainer, @NotNull ItemStack stack, @NotNull BlockSource source) {
        return tryCatchBBR(
                !isNull(callback.onDispenseCallback),
                (v) -> callback.onDispenseCallback.apply(module, moduleContainer, stack, source),
                (e) -> ICustomModule.super.onDispense(module, moduleContainer, stack, source)
        );
    }

    private void tryCatchBBV(boolean b, Runnable successAction, Runnable superAction) {
        if (b) {
            TryCatchPipe.pipeWithVoid(successAction, (e) -> superAction.run()).run();
        } else {
            superAction.run();
        }
    }

    private <T> T tryCatchBBR(boolean b, Function<Void, T> successAction, Function<Exception, T> superAction) {
        if (b) {
            return (T) TryCatchPipe.pipeWithReturnValue(successAction, superAction).run();
        } else {
            return superAction.apply(null);
        }
    }
}
