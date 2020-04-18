package me.zeroeightsix.waypoints.api

import me.zeroeightsix.waypoints.impl.WaypointRendererImpl
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f

interface WaypointRenderer {

    companion object {
        val default = WaypointRendererImpl
    }

    fun render(set: Set<RenderWaypoint>)

    val identifier: Identifier

}