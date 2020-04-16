package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Vec2f

object WaypointRendererImpl: WaypointRenderer {
    override fun render(waypoint: Waypoint, screenPos: Vec2f?) {
        if (screenPos == null) return
        val name = waypoint.name
        val textRenderer = MinecraftClient.getInstance().textRenderer
        textRenderer.draw(name, screenPos.x, MinecraftClient.getInstance().window.scaledHeight - screenPos.y, waypoint.colour)
    }
}