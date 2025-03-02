package com.anningui.modifyjs.mod_adder.mek.custom.module;


import com.anningui.modifyjs.callback.CustomInterface;
import com.anningui.modifyjs.mod_adder.mek.util.UnitItemSlots;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.gear.*;
import mekanism.api.gear.config.ModuleConfig;
import mekanism.api.providers.IItemProvider;
import mekanism.api.radial.RadialData;
import mekanism.api.radial.mode.IRadialMode;
import mekanism.api.radial.mode.NestedRadialMode;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;

import static com.anningui.modifyjs.mod_adder.mek.util.KubeJSMekUntiItemUtils.getItemById;

public class KubeJSModuleDataBuilder extends BuilderBase<ModuleData<KubeJSModuleData>> {
    public UnitItemSlots.Slots slot;
    private static final Set<KubeJSModuleDataBuilder> allBuilder = new HashSet<>();
    public transient ModuleData.ModuleDataBuilder<KubeJSModuleData> moduleDataBuilder;
    public transient KubeJSModuleCallback callback = new KubeJSModuleCallback();
    public transient int exclusive;
    public transient boolean handlesModeChange = false;
    public transient boolean modeChangeDisabledByDefault = false;
    public transient boolean rendersHUD = false;
    public transient boolean noDisable = false;
    public transient boolean disabledByDefault = false;
    public transient IntFunction defaultConfig;
    public transient IntFunction codec;
    public transient IntFunction streamCodec;
    public int maxStackSize = 1;
    public KubeJSModuleDataBuilder(ResourceLocation id) {
        super(id);
        allBuilder.add(this);
    }

    @HideFromJS
    public static Set<KubeJSModuleDataBuilder> getAllModuleDataBuilder() {
        return allBuilder;
    }

    public KubeJSModuleDataBuilder slot(UnitItemSlots.Slots slot) {
        this.slot = slot;
        return this;
    }

    public KubeJSModuleDataBuilder maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }
    public KubeJSModuleDataBuilder exclusive(int i) {
        this.exclusive = i;
        return this;
    }

    public KubeJSModuleDataBuilder handlesModeChange(boolean handlesModeChange) {
        this.handlesModeChange = handlesModeChange;
        return this;
    }

    public KubeJSModuleDataBuilder modeChangeDisabledByDefault(boolean modeChangeDisabledByDefault) {
        this.modeChangeDisabledByDefault = modeChangeDisabledByDefault;
        return this;
    }

    public KubeJSModuleDataBuilder rendersHUD(boolean name) {
        this.rendersHUD = name;
        return this;
    }

    public KubeJSModuleDataBuilder noDisable(boolean name) {
        this.noDisable = name;
        return this;
    }

    public KubeJSModuleDataBuilder disabledByDefault(boolean name) {
        this.disabledByDefault = name;
        return this;
    }

    public KubeJSModuleDataBuilder setExclusiveByFlag(ModuleData.ExclusiveFlag... flags) {
        this.exclusive = flags.length == 0 ? ModuleData.ExclusiveFlag.ANY : ModuleData.ExclusiveFlag.getCompoundMask(flags);
        return this;
    }

    public <TYPE, CONFIG extends ModuleConfig<TYPE>> KubeJSModuleDataBuilder addInstalledCountConfig(IntFunction<CONFIG> defaultConfig, IntFunction<Codec<CONFIG>> codec, IntFunction<StreamCodec<? super RegistryFriendlyByteBuf, CONFIG>> streamCodec) {
        this.defaultConfig = defaultConfig;
        this.codec = codec;
        this.streamCodec = streamCodec;
        return this;
    }

    public KubeJSModuleDataBuilder tickServer(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Void> tickServerCallback) {
        this.callback.tickServerCallback = tickServerCallback;
        return this;
    }

    public KubeJSModuleDataBuilder tickClient(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Void> tickClientCallback) {
        this.callback.tickClientCallback = tickClientCallback;
        return this;
    }

    public KubeJSModuleDataBuilder addHUDStrings(CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Consumer<Component>, Void> addHUDStringsCallback) {
        this.callback.addHUDStringsCallback = addHUDStringsCallback;
        return this;
    }

    public KubeJSModuleDataBuilder addHUDElements(CustomInterface.KQuintConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Player, Consumer<IHUDElement>, Void> addHUDElementsCallback) {
        this.callback.addHUDElementsCallback = addHUDElementsCallback;
        return this;
    }

    public KubeJSModuleDataBuilder canChangeModeWhenDisabled(Function<IModule<KubeJSModuleData>, Boolean> canChangeModeWhenDisabledCallback) {
        this.callback.canChangeModeWhenDisabledCallback = canChangeModeWhenDisabledCallback;
        return this;
    }

    public KubeJSModuleDataBuilder canChangeRadialModeWhenDisabled(Function<IModule<KubeJSModuleData>, Boolean> canChangeRadialModeWhenDisabledCallback) {
        this.callback.canChangeRadialModeWhenDisabledCallback = canChangeRadialModeWhenDisabledCallback;
        return this;
    }

    public KubeJSModuleDataBuilder getModeScrollComponent(BiFunction<IModule<KubeJSModuleData>, ItemStack, Component> getModeScrollComponentCallback) {
        this.callback.getModeScrollComponentCallback = getModeScrollComponentCallback;
        return this;
    }

    public KubeJSModuleDataBuilder changeMode(CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, IModuleContainer, ItemStack, Integer, Boolean, Void> changeModeCallback) {
        this.callback.changeModeCallback = changeModeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder addRadialModes(CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, Consumer<NestedRadialMode>, Void> addRadialModesCallback) {
        this.callback.addRadialModesCallback = addRadialModesCallback;
        return this;
    }

    public KubeJSModuleDataBuilder getMode(CustomInterface.KTriConsumer<IModule<KubeJSModuleData>, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode> getModeCallback) {
        this.callback.getModeCallback = getModeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder setMode(CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, IModuleContainer, ItemStack, RadialData<? extends IRadialMode>, ? extends IRadialMode, Boolean> setModeCallback) {
        this.callback.setModeCallback = setModeCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onAdded(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Boolean, Void> onAddedCallback) {
        this.callback.onAddedCallback = onAddedCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onRemoved(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, Boolean, Void> onRemovedCallback) {
        this.callback.onRemovedCallback = onRemovedCallback;
        return this;
    }

    public KubeJSModuleDataBuilder getDamageAbsorbInfo(BiFunction<IModule<KubeJSModuleData>, DamageSource, ICustomModule.ModuleDamageAbsorbInfo> getDamageAbsorbInfoCallback) {
        this.callback.getDamageAbsorbInfoCallback = getDamageAbsorbInfoCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onItemUse(BiFunction<IModule<KubeJSModuleData>, UseOnContext, InteractionResult> onItemUseCallback) {
        this.callback.onItemUseCallback = onItemUseCallback;
        return this;
    }

    public KubeJSModuleDataBuilder canPerformAction(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, ItemAbility, Boolean> canPerformActionCallback) {
        this.callback.canPerformActionCallback = canPerformActionCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onInteract(CustomInterface.KSextConsumer<IModule<KubeJSModuleData>, Player, LivingEntity, InteractionHand, IModuleContainer, ItemStack, InteractionResult> onInteractCallback) {
        this.callback.onInteractCallback = onInteractCallback;
        return this;
    }

    public KubeJSModuleDataBuilder onDispense(CustomInterface.KQuadConsumer<IModule<KubeJSModuleData>, IModuleContainer, ItemStack, BlockSource, ICustomModule.ModuleDispenseResult> onDispenseCallback) {
        this.callback.onDispenseCallback = onDispenseCallback;
        return this;
    }

    private KubeJSModuleDataBuilder self() {
        return this;
    }

    protected Supplier<ModuleData.ModuleDataBuilder<KubeJSModuleData>> bindBuilder() {
        UnaryOperator<ModuleData.ModuleDataBuilder<KubeJSModuleData>> a = builder -> {
            var bb = builder.maxStackSize(maxStackSize);
            if (exclusive != 0) {
                bb = bb.exclusive(exclusive);
            }
            if (handlesModeChange) {
                bb = bb.handlesModeChange();
            }
            if (modeChangeDisabledByDefault) {
                bb = bb.modeChangeDisabledByDefault();
            }
            if (rendersHUD) {
                bb = bb.rendersHUD();
            }
            if (noDisable) {
                bb = bb.noDisable();
            }
            if (disabledByDefault) {
                bb = bb.disabledByDefault();
            }
            if (defaultConfig != null && codec != null && streamCodec != null) {
                bb = bb.addInstalledCountConfig(defaultConfig, codec, streamCodec);
            }
            return bb;
        };
        Function<IModule<KubeJSModuleData>, KubeJSModuleData> s = (im) -> new KubeJSModuleData(im) {
            @Override
            public KubeJSModuleDataBuilder getBuilder() {
                return self();
            }
        };
        IItemProvider i = () -> Objects.requireNonNull(getItemById(id)).asItem();
        var e = ModuleData.ModuleDataBuilder.custom(s, i);
        return () -> a.apply(e);
    }
    @Override
    public ModuleData<KubeJSModuleData> createObject() {
        return new ModuleData<>(bindBuilder().get());
    }
}