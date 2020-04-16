package me.zeroeightsix.waypoints.api

import me.zeroeightsix.waypoints.impl.WaypointsImpl

interface Waypoints {

    companion object {
        @JvmStatic
        val accessor: Waypoints
            get() = WaypointsImpl
    }

    val waypoints: List<Waypoint>

    fun addWaypoint(waypoint: Waypoint)
    fun removeWaypoint(waypoint: Waypoint): Boolean

}