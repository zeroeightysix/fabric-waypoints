package me.zeroeightsix.waypoints.impl

import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer
import io.github.fablabsmc.fablabs.impl.fiber.serialization.FiberSerialization
import me.zeroeightsix.waypoints.api.WaypointRenderer
import me.zeroeightsix.waypoints.impl.WaypointsImpl.config
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
import java.nio.file.Files
import java.nio.file.Paths

@Environment(EnvType.CLIENT)
object WaypointsInitializer : ClientModInitializer {

    private val configBind: FabricKeyBinding = FabricKeyBinding.Builder.create(
        identifier("waypoints.bind.config"),
        InputUtil.Type.KEYSYM,
        InputUtil.UNKNOWN_KEYCODE.keyCode,
        "Waypoints"
    ).build()

    private val newWaypointBind: FabricKeyBinding = FabricKeyBinding.Builder.create(
        identifier("waypoints.bind.waypoint.new"),
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_N,
        "Waypoints"
    ).build()

    override fun onInitializeClient() {
        Registry.register(Registry.REGISTRIES, WaypointRendererRegistry.identifier, WaypointRendererRegistry)
        Registry.register(WaypointRendererRegistry, WaypointRenderer.default.identifier, WaypointRenderer.default)

        FiberSerialization.deserialize(
            config,
            Files.newInputStream(Paths.get("waypoints.json5")),
            JanksonValueSerializer(false)
        )

        WaypointsImpl.refresh()

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