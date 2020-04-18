package me.zeroeightsix.waypoints.api

import net.minecraft.util.math.Vec3d

interface Waypoint {

    var position: Vec3d
    var server: String
    var world: String
    var name: String
    val renderer: WaypointRenderer

}