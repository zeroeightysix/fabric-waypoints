package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.WaypointRenderer
import me.zeroeightsix.waypoints.impl.screen.NewWaypointScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry
import net.fabricmc.fabric.api.event.client.ClientTickCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.util.registry.Registry
import org.lwjgl.glfw.GLFW

@Environment(EnvType.CLIENT)
object WaypointsInitializer : ClientModInitializer {

    private val configBind: FabricKeyBinding = FabricKeyBinding.Builder.create(
        identifier("keybind.config"),
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_N,
        "Waypoints"
    ).build()

    private val newWaypointBind: FabricKeyBinding = FabricKeyBinding.Builder.create(
        identifier("keybind.new.waypoint"),
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_N,
        "Waypoints"
    ).build()

    override fun onInitializeClient() {
        Registry.register(Registry.REGISTRIES, WaypointRegistry.identifier, WaypointRegistry)
        Registry.register(WaypointRegistry, WaypointRenderer.default.identifier, WaypointRenderer.default)

        with(KeyBindingRegistry.INSTANCE) {
            addCategory("Waypoints")
            register(configBind)
            register(newWaypointBind)
        }

        ClientTickCallback.EVENT.register(ClientTickCallback {
            while (newWaypointBind.wasPressed()) {
                MinecraftClient.getInstance().openScreen(NewWaypointScreen())
            }
        })
    }

}