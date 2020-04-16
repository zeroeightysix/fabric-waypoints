package me.zeroeightsix.waypoints.api

import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

interface Waypoint {

    var position: Vec3d
    var name: String
    val renderer: WaypointRenderer

}