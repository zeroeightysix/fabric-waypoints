package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.DefaultedRegistry

const val id = "waypoints:renderers"

object WaypointRendererRegistry : DefaultedRegistry<WaypointRenderer>(id) {

    val identifier = Identifier(id)

}