package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.fiber.aggregate
import me.zeroeightsix.fiber.comment
import me.zeroeightsix.fiber.listener
import me.zeroeightsix.fiber.rootNode
import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.Waypoints
import net.minecraft.util.registry.Registry

object WaypointsImpl: Waypoints {

    @Suppress("unused")
    fun init() {
        Registry.register(Registry.REGISTRIES, WaypointRegistry.identifier, WaypointRegistry)
    }

    override val config = rootNode {
        aggregate("waypoints", listOf<Waypoint>()) {
            comment { "List of waypoints to render" }
            // the list is immutable, so you'll have to use setValue to change it.
            listener { list -> saveConfig(list) }
        }
    }

    private fun saveConfig(list: List<Waypoint>) {

    }


}

