package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Vec2f
import java.text.DecimalFormat
import kotlin.math.sqrt

object WaypointRendererImpl: WaypointRenderer {
    private val closeFormat: DecimalFormat = DecimalFormat("#.0")

    override fun render(waypoint: Waypoint, screenPos: Vec2f?) {
        val player = MinecraftClient.getInstance().player
        if (screenPos == null || player == null) return
        val name = waypoint.name
        val distance = sqrt(player.squaredDistanceTo(waypoint.position))

        val distanceFormatted = when {
            distance < 100 -> closeFormat.format(distance) + "m"
            distance < 2000 -> distance.toInt().toString() + "m"
            else -> closeFormat.format(distance / 1000) + "km"
        }

        val textRenderer = MinecraftClient.getInstance().textRenderer
        val height = textRenderer.fontHeight / 2 + 2

        textRenderer.drawWithShadow(name, screenPos.x - textRenderer.getStringWidth(name) / 2, MinecraftClient.getInstance().window.scaledHeight - screenPos.y - height, waypoint.colour)
        textRenderer.drawWithShadow(
            distanceFormatted,
            screenPos.x - textRenderer.getStringWidth(distanceFormatted) / 2,
            MinecraftClient.getInstance().window.scaledHeight - screenPos.y + height,
            waypoint.colour
        )
    }
}