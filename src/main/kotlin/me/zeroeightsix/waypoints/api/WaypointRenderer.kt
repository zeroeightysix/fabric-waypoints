package me.zeroeightsix.waypoints.api

import blue.endless.jankson.annotation.Deserializer
import blue.endless.jankson.annotation.Serializer
import me.zeroeightsix.waypoints.impl.WaypointRendererImpl
import me.zeroeightsix.waypoints.impl.WaypointRendererRegistry
import net.minecraft.util.Identifier

interface WaypointRenderer {

    companion object {
        val default = WaypointRendererImpl

        @JvmStatic @Deserializer
        fun fromString(s: String?): WaypointRenderer = WaypointRendererRegistry.get(Identifier(s))

        @JvmStatic @Serializer
        fun toString(r: WaypointRenderer) = r.identifier.toString()
    }

    fun render(set: Set<RenderWaypoint>)

    val identifier: Identifier

}