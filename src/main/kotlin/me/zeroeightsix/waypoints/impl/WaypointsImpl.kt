package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.fiber.tree.ConfigBranch
import me.zeroeightsix.fiber.tree.ConfigLeaf
import me.zeroeightsix.fiber.tree.ConfigTree
import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import me.zeroeightsix.waypoints.api.Waypoints
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

@Environment(EnvType.CLIENT)
object WaypointsImpl: Waypoints, ClientModInitializer {

    private lateinit var waypointsValue: ConfigLeaf<List<Waypoint>>

    override fun onInitializeClient() {
        Registry.register(Registry.REGISTRIES, WaypointRegistry.identifier, WaypointRegistry)
    }

    val config: ConfigBranch = ConfigTree.builder()
        .beginAggregateValue("waypoints", listOf<Waypoint>(
            WaypointImpl(
                position = Vec3d(10.0, 10.0, 10.0),
                name = "Test",
                renderer = WaypointRenderer.default
            )
        ), Waypoint::class.java)
        .withListener { _, _ ->  }
        .finishValue { v -> waypointsValue = v }
        .build()

    override val waypoints: List<Waypoint>
        get() = waypointsValue.value

}

