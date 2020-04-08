package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.Waypoint
import net.minecraft.util.math.Vec3d

data class WaypointImpl(
    override var colour: Int = 0xffffff, override var position: Vec3d, override var name: String
) : Waypoint