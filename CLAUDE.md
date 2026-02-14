# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ModifyJS is a NeoForge 1.21.1 Minecraft mod (mod ID: `modifyjs`) that extends KubeJS with custom item rendering, armor layer rendering, and Mekanism module item registration. It acts as a bridge allowing KubeJS script authors to use features the KubeJS community hasn't implemented — primarily client-side rendering hooks and Mekanism integration.

## Build Commands

- **Build:** `./gradlew build`
- **Run client:** `./gradlew runClient`
- **Run server:** `./gradlew runServer`
- **Run data generators:** `./gradlew runData`
- **Run game tests:** `./gradlew runGameTestServer`

Java 21 is required. The project uses NeoGradle 7.0 with Parchment mappings.

## Architecture

### Entry Points

- `ModifyJS` — Main `@Mod` entry point. Detects if Mekanism is loaded and sets `mekAdderIsOk`.
- `ModifyJSClient` — Client-side `@EventBusSubscriber` (MOD bus). Subscribes to NeoForge model/rendering events and delegates to the register classes.

### KubeJS Plugin System

Two plugins registered in `kubejs.plugins.txt`:
- `MJSKubeJSPlugin` — Registers the `MJSRenderEvents` event group and JS bindings (`TryCatchPipe`, `SwitchMap`, `MJSRenderUtils`, `MJSUtils`, `ModelResourceLocation`, etc.).
- `MJSMekKubeJSPlugin` — Conditionally registers the `modifyjs:mek_unit` item builder type and Mekanism utility bindings when Mekanism is present.

### Event Flow (KubeJS → NeoForge)

The mod exposes three KubeJS startup events via `MJSRenderEvents`:

1. **`MJSRenderEvents.itemRenderRegister`** → `ItemRenderRegister` — Maps items to custom `RenderByItemCallback`s. When NeoForge fires `RegisterClientExtensionsEvent`, registered items get wrapped in `KJSClientItemExtensions`. During `ModelEvent.ModifyBakingResult`, their baked models are wrapped in `MJSBakeModel`.

2. **`MJSRenderEvents.modelAdderRegister`** → `ModelAdditionalRegister` — Collects additional `ModelResourceLocation`s to register during `ModelEvent.RegisterAdditional`.

3. **`MJSRenderEvents.armorLayerRegister`** → `ArmorLayerRegister` — Maps armor items (or entity types) to render callbacks. During `EntityRenderersEvent.AddLayers`, adds custom render layers (`MJSHumanoidArmorLayer`, `MJSHorseArmorLayer`, `MJSRenderLayer`) to entity renderers. Supports `register`, `registerNoModel` (suppress vanilla model), and `registerCustom` (per entity type).

### Mixin Targets

Defined in `mixins.modifyjs.json`:
- **Common:** `ItemMekaToolMixin` (Mekanism MekaTool interaction)
- **Client:** `HumanoidArmorLayerMixin`, `HorseArmorLayerMixin`, `LivingEntityRendererMixin` — capture inner/outer armor models and layer lists into `MixinStore` for use by the armor layer system.

### Mekanism Integration (`mod_adder/mek/`)

When Mekanism is loaded:
- `KubeJSUnitItemBuilder` — KubeJS item builder for Mekanism module unit items (type `modifyjs:mek_unit`).
- `KubeJSModuleData` / `KubeJSModuleDataBuilder` — Define module data with slot assignment, callbacks (`onAdded`, `onRemoved`, `onEnabledStateChange`, etc.).
- During `InterModEnqueueEvent`, module items are dispatched to the correct Mekanism IMC slots (ALL, MEK_TOOL, MEK_SUIT_HELMET, etc.).

### Access Transformers

`META-INF/accesstransformer.cfg` opens:
- `EntityRenderDispatcher.renderers` — to iterate all entity renderers for adding armor layers.
- Several `HumanoidArmorLayer` methods (`renderModel`, `renderArmorPiece`, `getArmorModel`) — for custom armor rendering.
- `HorseArmorLayer.model` — for horse armor rendering.
- `ArmorMaterial.Layer.dyeable` — for armor dyeing checks.

## Key Dependencies

| Dependency   | Version Property                      | Purpose                  |
|--------------|---------------------------------------|--------------------------|
| NeoForge     | `neo_version` (21.1.114)              | Mod loader               |
| KubeJS       | `kubejs_version` (2101.7.1-build.181) | Scripting framework      |
| Rhino        | `rhino_version` (2101.2.6-build.67)   | JS engine for KubeJS     |
| Mekanism     | `mekanism_version` (10.7.5.62)        | Optional integration     |
| Architectury | `architectury_version` (13.0.2)       | Cross-platform utilities |

Version properties are in `gradle.properties`.

## Conventions

- Package root: `com.anningui.modifyjs`
- Static maps (e.g., `itemRendererMap`, `allArmorLayers`, `noShowModelMap`) hold registrations collected from KubeJS scripts and consumed by NeoForge event handlers.
- Methods hidden from KubeJS scripts are annotated with `@HideFromJS`.
- The `example/` directory contains KubeJS startup scripts demonstrating all three event types and a Mekanism unit item.
- The `libs/` directory contains local jar dependencies (currently ProbeJS).
