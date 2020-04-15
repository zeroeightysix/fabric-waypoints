package me.zeroeightsix.waypoints.api

import me.zeroeightsix.fiber.tree.ConfigNode
import me.zeroeightsix.waypoints.impl.WaypointsImpl

interface Waypoints {

    val instance: Waypoints
        get() = WaypointsImpl

    val config: ConfigNode

}