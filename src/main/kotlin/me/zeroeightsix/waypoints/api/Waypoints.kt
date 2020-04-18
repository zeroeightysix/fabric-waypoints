package me.zeroeightsix.waypoints.api

import me.zeroeightsix.waypoints.impl.WaypointsImpl

interface Waypoints {

    companion object {
        @JvmStatic
        val accessor: Waypoints
            get() = WaypointsImpl
    }

    /**
     * Keys are the waypoints' renderer identifier
     */
    val waypoints: Map<WaypointRenderer, List<Waypoint>>

    fun addWaypoint(waypoint: Waypoint)
    fun removeWaypoint(waypoint: Waypoint): Boolean

}