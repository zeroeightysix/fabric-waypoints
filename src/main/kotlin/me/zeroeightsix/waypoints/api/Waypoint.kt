package me.zeroeightsix.waypoints.api

import net.minecraft.util.math.Vec3d

interface Waypoint {

    var colour: Int
    var position: Vec3d
    var name: String

}