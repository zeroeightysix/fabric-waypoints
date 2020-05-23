package me.zeroeightsix.waypoints.api

import me.zeroeightsix.waypoints.impl.WaypointImpl
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
    val waypoints: Map<WaypointRenderer, List<WaypointImpl>>

    fun addWaypoint(waypoint: WaypointImpl)
    fun removeWaypoint(waypoint: WaypointImpl): Boolean

}