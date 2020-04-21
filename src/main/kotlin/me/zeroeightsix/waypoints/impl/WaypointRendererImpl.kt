package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.RenderWaypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.sqrt

object WaypointRendererImpl: WaypointRenderer {
    private val closeFormat: DecimalFormat = DecimalFormat("#.0")

    class Box(private val topLeft: Vec2f, private val bottomRight: Vec2f) {
        fun intersect(other: Box): Boolean {
            return (topLeft.x <= other.bottomRight.x &&
                    other.topLeft.x <= bottomRight.x &&
                    topLeft.y <= other.bottomRight.y &&
                    other.topLeft.y <= bottomRight.y)
        }
    }
    class CompiledWaypoint(val source: RenderWaypoint, val box: Box, val distance: String, var batch: MutableSet<CompiledWaypoint>?)

    override fun render(set: Set<RenderWaypoint>) {
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val player = MinecraftClient.getInstance().player ?: return
        val boxHeight = (textRenderer.fontHeight * 2 + 2).toFloat()

        set.map {
            val distance = sqrt(player.squaredDistanceTo(it.waypoint.position))

            val distanceFormatted = when {
                distance < 100 -> closeFormat.format(distance) + "m"
                distance < 2000 -> distance.toInt().toString() + "m"
                else -> closeFormat.format(distance / 1000) + "km"
            }

            val width = max(textRenderer.getStringWidth(it.waypoint.name), textRenderer.getStringWidth(distanceFormatted)).toFloat()
            val topLeft = it.screenPos - Vec2f(width / 2, boxHeight)

            val box = Box(
                topLeft,
                topLeft + Vec2f(width, boxHeight)
            )

            CompiledWaypoint(
                it,
                box,
                distanceFormatted,
                null
            )
        }.sortedBy { it.source.waypoint.name }.chunk().forEach { batch ->
            if (batch.size == 1) {
                val compiledWaypoint = batch.find { true } ?: return@forEach
                val name = compiledWaypoint.source.waypoint.name
                val distance = compiledWaypoint.distance
                textRenderer.drawWithShadow(name, compiledWaypoint.source.screenPos.x - textRenderer.getStringWidth(name) / 2, compiledWaypoint.source.screenPos.y - textRenderer.fontHeight / 2 - 1, 0xffffff)
                textRenderer.drawWithShadow(distance, compiledWaypoint.source.screenPos.x - textRenderer.getStringWidth(distance) / 2, compiledWaypoint.source.screenPos.y + textRenderer.fontHeight / 2 + 1, 0xffffff)
            } else {
                var centre = Vec2f.ZERO
                batch.forEach { centre += it.source.screenPos }
                centre /= batch.size

                var y = centre.y - ((textRenderer.fontHeight + 2) * batch.size) / 2
                batch.forEach {
                    val name = it.source.waypoint.name
                    val distance = it.distance
                    val label = "$name @ $distance"
                    textRenderer.drawWithShadow(label, centre.x - textRenderer.getStringWidth(label) / 2, y, 0xffffff)
                    y += textRenderer.fontHeight + 2
                }
            }
        }


    }

    override val identifier: Identifier
        get() = Identifier("waypoints:default_renderer")

    private fun List<CompiledWaypoint>.chunk(): Set<MutableSet<CompiledWaypoint>> {

        iterator().forEach { cw1 ->
            iterator().forEach { cw2 ->
                if (cw2 == cw1) return@forEach
                if (cw2.box.intersect(cw1.box)) {
                    val batch = when {
                        cw2.batch == null && cw1.batch == null -> mutableSetOf(cw1, cw2)
                        cw2.batch != null && cw1.batch != null -> (cw2.batch!! union cw1.batch!!).toMutableSet()
                        cw2.batch == null -> cw1.batch.also { b -> b!!.add(cw2) }
                        else -> cw2.batch.also { it!!.add(cw1) }
                    }
                    cw2.batch = batch
                    cw1.batch = batch
                }
            }
        }

        return map { it.batch ?: mutableSetOf(it) }.toSet()

    }

}

private operator fun Vec2f.plus(vec2f: Vec2f): Vec2f = Vec2f(x + vec2f.x, y + vec2f.y)
private operator fun Vec2f.minus(vec2f: Vec2f): Vec2f = Vec2f(x - vec2f.x, y - vec2f.y)
private operator fun Vec2f.div(factor: Int): Vec2f = Vec2f(x / factor, y / factor)
