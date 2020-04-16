package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

data class WaypointImpl(
    override var position: Vec3d,
    override var name: String,
    override val renderer: WaypointRenderer
) : Waypoint