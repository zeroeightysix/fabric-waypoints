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

object WaypointsImpl: Waypoints {

    private lateinit var configWaypoints: ConfigLeaf<List<Waypoint>>

    private val config: ConfigBranch = ConfigTree.builder()
        .beginAggregateValue("waypoints", List::class.java, Waypoint::class.java, listOf<Waypoint>(
            WaypointImpl(
                position = Vec3d(10.0, 10.0, 10.0),
                name = "Test",
                renderer = WaypointRenderer.default
            )
        ))
        .withListener { _, _ -> }
        .finishValue { v -> configWaypoints = v }
        .build()

    override val waypoints: List<Waypoint>
        get() = configWaypoints.value

    override fun addWaypoint(waypoint: Waypoint) {
        configWaypoints.value.toMutableList().also {
            it.add(waypoint)
            configWaypoints.value = it
        }
    }

    override fun removeWaypoint(waypoint: Waypoint): Boolean {
        val l = configWaypoints.value.toMutableList()
        val r = l.remove(waypoint)
        configWaypoints.value = l
        return r
    }

}

