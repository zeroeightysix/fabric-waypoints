package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.fiber.serialization.JanksonSerializer
import me.zeroeightsix.fiber.tree.ConfigBranch
import me.zeroeightsix.fiber.tree.ConfigLeaf
import me.zeroeightsix.fiber.tree.ConfigTree
import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import me.zeroeightsix.waypoints.api.Waypoints
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Environment(EnvType.CLIENT)
object WaypointsImpl: Waypoints, ClientModInitializer {

    private lateinit var waypointsValue: ConfigLeaf<List<Waypoint>>
    private val serializer = JanksonSerializer()
    private val configPath = Paths.get("waypoints.json5")

    override fun onInitializeClient() {
        Registry.register(Registry.REGISTRIES, WaypointRegistry.identifier, WaypointRegistry)
        Registry.register(WaypointRegistry, Identifier("waypoints", "default_renderer"), WaypointRenderer.default)

        if (Files.exists(configPath))
            serializer.deserialize(config, Files.newInputStream(Paths.get("waypoints.json5"), StandardOpenOption.CREATE))

        saveConfig()
    }

    private val config: ConfigBranch = ConfigTree.builder()
        .beginAggregateValue("waypoints", List::class.java, Waypoint::class.java, listOf<Waypoint>(
            WaypointImpl(
                position = Vec3d(10.0, 10.0, 10.0),
                name = "Test",
                renderer = WaypointRenderer.default
            )
        ))
        .withListener { _, _ -> saveConfig() }
        .finishValue { v -> waypointsValue = v }
        .build()

    private fun saveConfig() {
        serializer.serialize(config, Files.newOutputStream(Paths.get("waypoints.json5")))
    }

    override val waypoints: List<Waypoint>
        get() = waypointsValue.value

    override fun addWaypoint(waypoint: Waypoint) {
        val list = waypointsValue.value.toMutableList()
        list.add(waypoint)
        waypointsValue.value = list
    }

    override fun removeWaypoint(waypoint: Waypoint): Boolean {
        val list = waypointsValue.value.toMutableList()
        return list.remove(waypoint).also {
            waypointsValue.value = list
        }
    }

}

