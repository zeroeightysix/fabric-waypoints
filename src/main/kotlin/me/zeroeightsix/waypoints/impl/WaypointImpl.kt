package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.util.math.Vec3d

data class WaypointImpl(
    override var colour: Int = 0xffffff,
    override var position: Vec3d,
    override var name: String,
    override val renderer: WaypointRenderer
) : Waypoint